package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import net.srt.convert.DataProductionCatalogueConvert;
import net.srt.convert.DataProductionTaskConvert;
import net.srt.dao.DataProductionCatalogueDao;
import net.srt.entity.DataProductionCatalogueEntity;
import net.srt.entity.DataProductionTaskEntity;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.common.utils.BeanUtil;
import net.srt.framework.common.utils.BuildTreeUtils;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.service.DataProductionCatalogueService;
import net.srt.service.DataProductionTaskService;
import net.srt.vo.DataProductionCatalogueVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.List;

/**
 * 数据生产目录
 *
 * @author zrx 985134801@qq.com
 * @since 2.0.0 2022-11-27
 */
@Service
@AllArgsConstructor
public class DataProductionCatalogueServiceImpl extends BaseServiceImpl<DataProductionCatalogueDao, DataProductionCatalogueEntity> implements DataProductionCatalogueService {

	private final DataProductionTaskService taskService;


	@Override
	public void save(DataProductionCatalogueVO vo) {
		DataProductionCatalogueEntity catalogueEntity = DataProductionCatalogueConvert.INSTANCE.convert(vo);
		catalogueEntity.setPath(recursionPath(catalogueEntity, null));
		catalogueEntity.setProjectId(getProjectId());
		baseMapper.insert(catalogueEntity);
		if (catalogueEntity.getTaskType() != null) {
			//添加任务
			DataProductionTaskEntity taskEntity = new DataProductionTaskEntity();
			taskEntity.setProjectId(catalogueEntity.getProjectId());
			taskEntity.setCatalogueId(catalogueEntity.getId());
			taskEntity.setName(catalogueEntity.getName());
			taskEntity.setAlias(catalogueEntity.getName());
			taskEntity.setDialect(catalogueEntity.getTaskType());
			taskService.save(DataProductionTaskConvert.INSTANCE.convert(taskEntity));
		}


	}

	@Override
	public void update(DataProductionCatalogueVO vo) {
		DataProductionCatalogueEntity entity = DataProductionCatalogueConvert.INSTANCE.convert(vo);
		entity.setPath(recursionPath(entity, null));
		entity.setProjectId(getProjectId());
		updateById(entity);
		if (entity.getTaskId() != null) {
			//修改作业名称
			taskService.updateInfoByCatalogue(entity);
		}
	}

	private String recursionPath(DataProductionCatalogueEntity categoryEntity, String path) {
		if (StringUtil.isBlank(path)) {
			path = categoryEntity.getName();
		}
		if (categoryEntity.getParentId() != 0) {
			DataProductionCatalogueEntity parent = getById(categoryEntity.getParentId());
			path = parent.getName() + "/" + path;
			return recursionPath(parent, path);
		}
		return path;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) {
		//查看有无子节点
		LambdaQueryWrapper<DataProductionCatalogueEntity> wrapper = new LambdaQueryWrapper<>();
		if (baseMapper.selectOne(wrapper.eq(DataProductionCatalogueEntity::getParentId, id).last(" limit 1")) != null) {
			throw new ServerException("存在子节点，不可删除！");
		}
		//同步删除任务及其配置
		DataProductionCatalogueEntity catalogueEntity = baseMapper.selectById(id);
		if (catalogueEntity.getTaskId() != null) {
			taskService.delete(catalogueEntity.getTaskId());
		}
		removeById(id);
	}

	@Override
	public List<TreeNodeVo> listTree() {
		LambdaQueryWrapper<DataProductionCatalogueEntity> wrapper = new LambdaQueryWrapper<>();
		dataScopeWithOrgId(wrapper);
		wrapper.orderByAsc(DataProductionCatalogueEntity::getOrderNo);
		List<DataProductionCatalogueEntity> dataFileCategoryEntities = baseMapper.selectList(wrapper);
		List<TreeNodeVo> treeNodeVos = BeanUtil.copyListProperties(dataFileCategoryEntities, TreeNodeVo::new, (oldItem, newItem) -> {
			newItem.setLabel(oldItem.getName());
			if (newItem.getPath().contains("/")) {
				newItem.setParentPath(newItem.getPath().substring(0, newItem.getPath().lastIndexOf("/")));
			}
		});
		return BuildTreeUtils.buildTree(treeNodeVos);
	}

}
