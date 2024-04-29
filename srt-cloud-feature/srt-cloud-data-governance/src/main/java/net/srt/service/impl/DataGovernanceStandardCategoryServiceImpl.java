package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceStandardCategoryConvert;
import net.srt.dao.DataGovernanceStandardCategoryDao;
import net.srt.dao.DataGovernanceStandardDao;
import net.srt.entity.DataGovernanceMetamodelEntity;
import net.srt.entity.DataGovernanceStandardCategoryEntity;
import net.srt.entity.DataGovernanceStandardEntity;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.BeanUtil;
import net.srt.framework.common.utils.BuildTreeUtils;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataGovernanceStandardCategoryQuery;
import net.srt.service.DataGovernanceStandardCategoryService;
import net.srt.vo.DataGovernanceStandardCategoryVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.List;

/**
 * 数据治理-标准目录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-05
 */
@Service
@AllArgsConstructor
public class DataGovernanceStandardCategoryServiceImpl extends BaseServiceImpl<DataGovernanceStandardCategoryDao, DataGovernanceStandardCategoryEntity> implements DataGovernanceStandardCategoryService {

	private final DataGovernanceStandardDao standardDao;
	@Override
	public List<TreeNodeVo> listTree() {
		LambdaQueryWrapper<DataGovernanceStandardCategoryEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataGovernanceStandardCategoryEntity::getProjectId, getProjectId()).orderByAsc(DataGovernanceStandardCategoryEntity::getOrderNo);
		List<DataGovernanceStandardCategoryEntity> dataGovernanceStandardCategoryEntities = baseMapper.selectList(wrapper);
		List<TreeNodeVo> treeNodeVos = BeanUtil.copyListProperties(dataGovernanceStandardCategoryEntities, TreeNodeVo::new, (oldItem, newItem) -> {
			newItem.setLabel(oldItem.getName());
			newItem.setValue(oldItem.getId());
			newItem.setDisabled(oldItem.getType() != 1);
			if (newItem.getPath().contains("/")) {
				newItem.setParentPath(newItem.getPath().substring(0, newItem.getPath().lastIndexOf("/")));
			}
		});
		return BuildTreeUtils.buildTree(treeNodeVos);
	}

	@Override
	public void save(DataGovernanceStandardCategoryVO vo) {
		DataGovernanceStandardCategoryEntity entity = DataGovernanceStandardCategoryConvert.INSTANCE.convert(vo);
		entity.setPath(recursionPath(entity, null));
		entity.setProjectId(getProjectId());
		baseMapper.insert(entity);
	}

	@Override
	public void update(DataGovernanceStandardCategoryVO vo) {
		DataGovernanceStandardCategoryEntity entity = DataGovernanceStandardCategoryConvert.INSTANCE.convert(vo);
		entity.setPath(recursionPath(entity, null));
		entity.setProjectId(getProjectId());
		updateById(entity);
	}

	private String recursionPath(DataGovernanceStandardCategoryEntity categoryEntity, String path) {
		if (StringUtil.isBlank(path)) {
			path = categoryEntity.getName();
		}
		if (categoryEntity.getParentId() != 0) {
			DataGovernanceStandardCategoryEntity parent = getById(categoryEntity.getParentId());
			path = parent.getName() + "/" + path;
			return recursionPath(parent, path);
		}
		return path;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) {
		LambdaQueryWrapper<DataGovernanceStandardCategoryEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataGovernanceStandardCategoryEntity::getParentId, id).last("limit 1");
		if (baseMapper.selectOne(wrapper) != null) {
			throw new ServerException("存在子节点，不可删除！");
		}
		LambdaQueryWrapper<DataGovernanceStandardEntity> standardWrapper = new LambdaQueryWrapper<>();
		standardWrapper.eq(DataGovernanceStandardEntity::getCategoryId, id).last("limit 1");
		if (standardDao.selectOne(standardWrapper) != null) {
			throw new ServerException("目录下存在数据标准配置，不可删除！");
		}
		removeById(id);
	}

}
