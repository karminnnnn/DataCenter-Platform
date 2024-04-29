package net.srt.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.AllArgsConstructor;
import net.srt.api.module.system.constant.SuperAdminEnum;
import net.srt.convert.DataGovernanceMetamodelConvert;
import net.srt.dao.DataGovernanceMetamodelDao;
import net.srt.entity.DataGovernanceMetamodelEntity;
import net.srt.framework.common.constant.Constant;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.common.utils.BeanUtil;
import net.srt.framework.common.utils.BuildTreeUtils;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.framework.security.user.SecurityUser;
import net.srt.framework.security.user.UserDetail;
import net.srt.service.DataGovernanceMetamodelService;
import net.srt.vo.DataGovernanceMetamodelVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据治理-元模型
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-03-28
 */
@Service
@AllArgsConstructor
public class DataGovernanceMetamodelServiceImpl extends BaseServiceImpl<DataGovernanceMetamodelDao, DataGovernanceMetamodelEntity> implements DataGovernanceMetamodelService {


	@Override
	public List<TreeNodeVo> listTree() {
		LambdaQueryWrapper<DataGovernanceMetamodelEntity> wrapper = new LambdaQueryWrapper<>();
		//查询当前项目下的元模型和公共的元模型
		UserDetail user = SecurityUser.getUser();
		wrapper.eq(DataGovernanceMetamodelEntity::getProjectId, getProjectId()).or().eq(DataGovernanceMetamodelEntity::getProjectId, 0)
				.orderByAsc(DataGovernanceMetamodelEntity::getOrderNo);
		List<Long> dataScopeList = user.getDataScopeList();
		List<Long> orgList;
		if (dataScopeList != null) {
			orgList = new ArrayList<>(dataScopeList);
		} else {
			orgList = new ArrayList<>();
		}
		orgList.add(0L);
		StringBuilder sqlFilter = new StringBuilder();
		if (!user.getSuperAdmin().equals(Constant.SUPER_ADMIN)) {
			// 机构数据过滤，如果角色分配了机构的数据权限，则过滤，仅适用于有机构id的表
			sqlFilter.append(" org_id");
			sqlFilter.append(" IN( ").append(StrUtil.join(",", orgList)).append(" ) ");
			wrapper.apply(sqlFilter.toString());
		}
		List<DataGovernanceMetamodelEntity> dataGovernanceMetamodelEntities = baseMapper.selectList(wrapper);
		List<TreeNodeVo> treeNodeVos = BeanUtil.copyListProperties(dataGovernanceMetamodelEntities, TreeNodeVo::new, (oldItem, newItem) -> {
			newItem.setLabel(oldItem.getName());
			newItem.setValue(oldItem.getId());
			newItem.setDisabled(oldItem.getIfLeaf() == 1);
			if (newItem.getPath().contains("/")) {
				newItem.setParentPath(newItem.getPath().substring(0, newItem.getPath().lastIndexOf("/")));
			}
		});
		return BuildTreeUtils.buildTree(treeNodeVos);
	}

	@Override
	public void save(DataGovernanceMetamodelVO vo) {
		DataGovernanceMetamodelEntity entity = DataGovernanceMetamodelConvert.INSTANCE.convert(vo);
		entity.setPath(recursionPath(entity, null));
		entity.setProjectId(getProjectId());
		entity.setBuiltin(0);
		buildField(entity);
		baseMapper.insert(entity);
	}

	@Override
	public void update(DataGovernanceMetamodelVO vo) {
		DataGovernanceMetamodelEntity entity = DataGovernanceMetamodelConvert.INSTANCE.convert(vo);
		entity.setPath(recursionPath(entity, null));
		entity.setProjectId(getProjectId());
		entity.setBuiltin(0);
		//TODO 后续改成动态
		buildField(entity);
		updateById(entity);
	}

	private void buildField(DataGovernanceMetamodelEntity entity) {
		//TODO 后续改成动态
		if (entity.getIfLeaf() == 0) {
			entity.setIcon("/src/assets/model.png");
		} else {
			entity.setIcon("/src/assets/folder.png");
		}
	}

	private String recursionPath(DataGovernanceMetamodelEntity metamodelEntity, String path) {
		if (StringUtil.isBlank(path)) {
			path = metamodelEntity.getName();
		}
		if (metamodelEntity.getParentId() != 0) {
			DataGovernanceMetamodelEntity parent = getById(metamodelEntity.getParentId());
			path = parent.getName() + "/" + path;
			return recursionPath(parent, path);
		}
		return path;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) {
		LambdaQueryWrapper<DataGovernanceMetamodelEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataGovernanceMetamodelEntity::getParentId, id).last("limit 1");
		if (baseMapper.selectOne(wrapper) != null) {
			throw new ServerException("存在子节点，不可删除！");
		}
		removeById(id);
	}

}
