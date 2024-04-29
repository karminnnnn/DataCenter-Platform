package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.constant.ApiGroupType;
import net.srt.convert.DataServiceApiGroupConvert;
import net.srt.dao.DataServiceApiGroupDao;
import net.srt.entity.DataServiceApiConfigEntity;
import net.srt.entity.DataServiceApiGroupEntity;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.common.utils.BeanUtil;
import net.srt.framework.common.utils.BuildTreeUtils;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.service.DataServiceApiConfigService;
import net.srt.service.DataServiceApiGroupService;
import net.srt.vo.DataServiceApiGroupVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据服务-api分组
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-01-28
 */
@Service
@AllArgsConstructor
public class DataServiceApiGroupServiceImpl extends BaseServiceImpl<DataServiceApiGroupDao, DataServiceApiGroupEntity> implements DataServiceApiGroupService {

	private final DataServiceApiConfigService apiConfigService;

	@Override
	public void save(DataServiceApiGroupVO vo) {
		DataServiceApiGroupEntity entity = DataServiceApiGroupConvert.INSTANCE.convert(vo);
		entity.setPath(recursionPath(entity, null));
		entity.setProjectId(getProjectId());
		baseMapper.insert(entity);
	}

	@Override
	public void update(DataServiceApiGroupVO vo) {
		DataServiceApiGroupEntity entity = DataServiceApiGroupConvert.INSTANCE.convert(vo);
		entity.setPath(recursionPath(entity, null));
		entity.setProjectId(getProjectId());
		updateById(entity);
		//更新apiCOnfig的orgId
		LambdaUpdateWrapper<DataServiceApiConfigEntity> wrapper = Wrappers.lambdaUpdate();
		wrapper.eq(DataServiceApiConfigEntity::getGroupId, vo.getId());
		wrapper.set(DataServiceApiConfigEntity::getOrgId, vo.getOrgId());
		apiConfigService.update(wrapper);
	}

	private String recursionPath(DataServiceApiGroupEntity groupEntity, String path) {
		if (StringUtil.isBlank(path)) {
			path = groupEntity.getName();
		}
		if (groupEntity.getParentId() != 0) {
			DataServiceApiGroupEntity parent = getById(groupEntity.getParentId());
			path = parent.getName() + "/" + path;
			return recursionPath(parent, path);
		}
		return path;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) {
		//查询有没有子节点
		LambdaQueryWrapper<DataServiceApiGroupEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataServiceApiGroupEntity::getParentId, id).last(" limit 1");
		DataServiceApiGroupEntity one = baseMapper.selectOne(wrapper);
		if (one != null) {
			throw new ServerException("存在子节点，不允许删除！");
		}
		//查询有没有api与之关联
		LambdaQueryWrapper<DataServiceApiConfigEntity> serviceApiConfigWrapper = new LambdaQueryWrapper<>();
		serviceApiConfigWrapper.eq(DataServiceApiConfigEntity::getGroupId, id).last(" limit 1");
		DataServiceApiConfigEntity apiConfigEntity = apiConfigService.getOne(serviceApiConfigWrapper);
		if (apiConfigEntity != null) {
			throw new ServerException("节点下有 api 与之关联，不允许删除！");
		}
		removeById(id);
	}

	@Override
	public List<TreeNodeVo> listTree() {
		List<TreeNodeVo> treeNodeVos = getTreeNodeVos();
		return BuildTreeUtils.buildTree(treeNodeVos);
	}

	private List<TreeNodeVo> getTreeNodeVos() {
		LambdaQueryWrapper<DataServiceApiGroupEntity> wrapper = new LambdaQueryWrapper<>();
		dataScopeWithOrgId(wrapper);
		wrapper.orderByAsc(DataServiceApiGroupEntity::getOrderNo);
		List<DataServiceApiGroupEntity> apiGroupEntities = baseMapper.selectList(wrapper);
		return BeanUtil.copyListProperties(apiGroupEntities, TreeNodeVo::new, (oldItem, newItem) -> {
			newItem.setLabel(oldItem.getName());
			newItem.setValue(oldItem.getId());
			newItem.setDisabled(oldItem.getType() == 1);
			if (newItem.getPath().contains("/")) {
				newItem.setParentPath(newItem.getPath().substring(0, newItem.getPath().lastIndexOf("/")));
			}
		});
	}

	@Override
	public List<TreeNodeVo> listTreeWithApi() {
		List<TreeNodeVo> treeNodeVos = getTreeNodeVos();
		List<TreeNodeVo> treeNodeVosWithApi = new ArrayList<>();
		for (TreeNodeVo treeNodeVo : treeNodeVos) {
			treeNodeVosWithApi.add(treeNodeVo);
			if (ApiGroupType.API.getValue().equals(treeNodeVo.getType())) {
				//查询底下已发布的api
				List<DataServiceApiConfigEntity> apis = apiConfigService.listActiveByGroupId(treeNodeVo.getId());
				for (DataServiceApiConfigEntity api : apis) {
					TreeNodeVo apiNode = new TreeNodeVo();
					apiNode.setId(api.getId());
					apiNode.setParentId(treeNodeVo.getId());
					apiNode.setParentPath(treeNodeVo.getPath());
					apiNode.setOrderNo(api.getId().intValue());
					apiNode.setLabel(api.getName());
					apiNode.setName(api.getName());
					treeNodeVosWithApi.add(apiNode);
				}
			}
		}
		return BuildTreeUtils.buildTree(treeNodeVosWithApi);
	}

}
