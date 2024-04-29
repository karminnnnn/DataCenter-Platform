package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceQualityConfigCategoryConvert;
import net.srt.dao.DataGovernanceQualityConfigCategoryDao;
import net.srt.dao.DataGovernanceQualityConfigDao;
import net.srt.entity.DataGovernanceQualityConfigCategoryEntity;
import net.srt.entity.DataGovernanceQualityConfigEntity;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.common.utils.BeanUtil;
import net.srt.framework.common.utils.BuildTreeUtils;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.service.DataGovernanceQualityConfigCategoryService;
import net.srt.vo.DataGovernanceQualityConfigCategoryVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.List;

/**
 * 数据治理-规则配置目录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-28
 */
@Service
@AllArgsConstructor
public class DataGovernanceQualityConfigCategoryServiceImpl extends BaseServiceImpl<DataGovernanceQualityConfigCategoryDao, DataGovernanceQualityConfigCategoryEntity> implements DataGovernanceQualityConfigCategoryService {

	private final DataGovernanceQualityConfigDao qualityConfigDao;

	@Override
	public List<TreeNodeVo> listTree() {
		LambdaQueryWrapper<DataGovernanceQualityConfigCategoryEntity> wrapper = new LambdaQueryWrapper<>();
		dataScopeWithOrgId(wrapper);
		wrapper.orderByAsc(DataGovernanceQualityConfigCategoryEntity::getOrderNo);
		List<DataGovernanceQualityConfigCategoryEntity> configCategoryEntities = baseMapper.selectList(wrapper);
		List<TreeNodeVo> treeNodeVos = BeanUtil.copyListProperties(configCategoryEntities, TreeNodeVo::new, (oldItem, newItem) -> {
			newItem.setLabel(oldItem.getName());
			newItem.setValue(oldItem.getId());
			if (newItem.getPath().contains("/")) {
				newItem.setParentPath(newItem.getPath().substring(0, newItem.getPath().lastIndexOf("/")));
			}
		});
		return BuildTreeUtils.buildTree(treeNodeVos);
	}

	@Override
	public void save(DataGovernanceQualityConfigCategoryVO vo) {
		DataGovernanceQualityConfigCategoryEntity entity = DataGovernanceQualityConfigCategoryConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		entity.setPath(recursionPath(entity, null));
		baseMapper.insert(entity);
	}

	@Override
	public void update(DataGovernanceQualityConfigCategoryVO vo) {
		DataGovernanceQualityConfigCategoryEntity entity = DataGovernanceQualityConfigCategoryConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		entity.setPath(recursionPath(entity, null));
		updateById(entity);
		//更新规则配置的orgId
		LambdaUpdateWrapper<DataGovernanceQualityConfigEntity> wrapper = Wrappers.lambdaUpdate();
		wrapper.eq(DataGovernanceQualityConfigEntity::getCategoryId, vo.getId());
		wrapper.set(DataGovernanceQualityConfigEntity::getOrgId, vo.getOrgId());
		qualityConfigDao.update(null, wrapper);
	}

	private String recursionPath(DataGovernanceQualityConfigCategoryEntity categoryEntity, String path) {
		if (StringUtil.isBlank(path)) {
			path = categoryEntity.getName();
		}
		if (categoryEntity.getParentId() != 0) {
			DataGovernanceQualityConfigCategoryEntity parent = getById(categoryEntity.getParentId());
			path = parent.getName() + "/" + path;
			return recursionPath(parent, path);
		}
		return path;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) {
		//判断是否有子目录
		LambdaQueryWrapper<DataGovernanceQualityConfigCategoryEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataGovernanceQualityConfigCategoryEntity::getParentId, id).last("limit 1");
		DataGovernanceQualityConfigCategoryEntity oneChild = baseMapper.selectOne(wrapper);
		if (oneChild != null) {
			throw new ServerException("存在子项，不可删除！");
		}
		LambdaQueryWrapper<DataGovernanceQualityConfigEntity> configWrapper = Wrappers.lambdaQuery();
		configWrapper.eq(DataGovernanceQualityConfigEntity::getCategoryId, id).last("limit 1");
		DataGovernanceQualityConfigEntity qualityConfigEntity = qualityConfigDao.selectOne(configWrapper);
		if (qualityConfigEntity != null) {
			throw new ServerException("目录下存在规则配置，不可删除！");
		}
		removeById(id);
	}

}
