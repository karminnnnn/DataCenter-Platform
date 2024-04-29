package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import net.srt.api.module.data.governance.constant.BuiltInMetamodel;
import net.srt.convert.DataGovernanceMetadataConvert;
import net.srt.dao.DataGovernanceMetadataDao;
import net.srt.dao.DataGovernanceMetadataPropertyDao;
import net.srt.dao.DataGovernanceMetamodelDao;
import net.srt.entity.DataGovernanceMetadataEntity;
import net.srt.entity.DataGovernanceMetadataPropertyEntity;
import net.srt.framework.common.cache.bean.Neo4jInfo;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.common.utils.BeanUtil;
import net.srt.framework.common.utils.BuildTreeUtils;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.framework.security.cache.TokenStoreCache;
import net.srt.service.DataGovernanceMetadataService;
import net.srt.vo.DataGovernanceMetadataVO;
import net.srt.vo.DataGovernanceMetamodelPropertyVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据治理-元数据
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-03-29
 */
@Service
@AllArgsConstructor
public class DataGovernanceMetadataServiceImpl extends BaseServiceImpl<DataGovernanceMetadataDao, DataGovernanceMetadataEntity> implements DataGovernanceMetadataService {

	private final DataGovernanceMetamodelDao metamodelDao;
	private final DataGovernanceMetadataPropertyDao metadataPropertyDao;
	private final TokenStoreCache tokenStoreCache;

	@Override
	public void save(DataGovernanceMetadataVO vo) {
		DataGovernanceMetadataEntity entity = DataGovernanceMetadataConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		entity.setPath(recursionPath(entity, null));
		buildField(entity);
		DataGovernanceMetadataEntity parentMetadata = baseMapper.selectById(vo.getParentId());
		if (parentMetadata != null) {
			entity.setDbType(parentMetadata.getDbType());
			entity.setDatasourceId(parentMetadata.getDatasourceId());
			entity.setCollectTaskId(parentMetadata.getCollectTaskId());
		}
		baseMapper.insert(entity);
		buildProperties(entity, vo.getProperties());
	}

	@Override
	public void update(DataGovernanceMetadataVO vo) {
		DataGovernanceMetadataEntity entity = DataGovernanceMetadataConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		entity.setPath(recursionPath(entity, null));
		buildField(entity);
		updateById(entity);
		buildProperties(entity, vo.getProperties());
	}

	private void buildProperties(DataGovernanceMetadataEntity entity, List<DataGovernanceMetamodelPropertyVO> properties) {
		if (!CollectionUtils.isEmpty(properties)) {
			LambdaQueryWrapper<DataGovernanceMetadataPropertyEntity> wrapper = new LambdaQueryWrapper<>();
			wrapper.eq(DataGovernanceMetadataPropertyEntity::getMetadataId, entity.getId());
			for (DataGovernanceMetamodelPropertyVO property : properties) {
				DataGovernanceMetadataPropertyEntity metadataPropertyEntity = new DataGovernanceMetadataPropertyEntity();
				metadataPropertyEntity.setMetamodelPropertyId(property.getId());
				metadataPropertyEntity.setMetadataId(entity.getId());
				metadataPropertyEntity.setProperty(property.getValue());
				metadataPropertyEntity.setProjectId(entity.getProjectId());
				//判断是否有属性值的主键id，有则更新，无则新增
				if (property.getMetadataPropertyId() != null) {
					metadataPropertyEntity.setId(property.getMetadataPropertyId());
					metadataPropertyDao.updateById(metadataPropertyEntity);
				} else {
					metadataPropertyDao.insert(metadataPropertyEntity);
				}

			}
		}
	}

	private void buildField(DataGovernanceMetadataEntity entity) {
		if (entity.getMetamodelId() != null) {
			entity.setIcon(metamodelDao.selectById(entity.getMetamodelId()).getIcon());
		}
		//TODO 后续改成动态
		if (entity.getIfLeaf() == 1 && entity.getMetamodelId() == null) {
			entity.setIcon("/src/assets/folder.png");
		}
	}

	private String recursionPath(DataGovernanceMetadataEntity metadataEntity, String path) {
		if (StringUtil.isBlank(path)) {
			path = metadataEntity.getName();
		}
		if (metadataEntity.getParentId() != 0) {
			DataGovernanceMetadataEntity parent = getById(metadataEntity.getParentId());
			path = parent.getName() + "/" + path;
			return recursionPath(parent, path);
		}
		return path;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) {
		//判断是否有子节点
		LambdaQueryWrapper<DataGovernanceMetadataEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataGovernanceMetadataEntity::getParentId, id).last("limit 1");
		if (baseMapper.selectOne(wrapper) != null) {
			throw new ServerException("存在子节点，不可删除！");
		}
		removeById(id);
		//删除属性
		LambdaQueryWrapper<DataGovernanceMetadataPropertyEntity> propertyWrapper = new LambdaQueryWrapper<>();
		propertyWrapper.eq(DataGovernanceMetadataPropertyEntity::getMetadataId, id);
		metadataPropertyDao.delete(propertyWrapper);
	}

	@Override
	public List<TreeNodeVo> listByPatentId(Long parentId) {
		LambdaQueryWrapper<DataGovernanceMetadataEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataGovernanceMetadataEntity::getParentId, parentId).orderByAsc(DataGovernanceMetadataEntity::getOrderNo);
		dataScopeWithOrgId(wrapper);
		List<DataGovernanceMetadataEntity> dataGovernanceMetadataEntities = baseMapper.selectList(wrapper);
		return BeanUtil.copyListProperties(dataGovernanceMetadataEntities, TreeNodeVo::new, (oldItem, newItem) -> {
			newItem.setLabel(oldItem.getName());
			newItem.setValue(oldItem.getId());
			newItem.setLeaf(BuiltInMetamodel.COLUMN.getId().equals(oldItem.getMetamodelId()));
			newItem.setDisabled(!BuiltInMetamodel.COLUMN.getId().equals(oldItem.getMetamodelId()));
			if (newItem.getPath().contains("/")) {
				newItem.setParentPath(newItem.getPath().substring(0, newItem.getPath().lastIndexOf("/")));
			}
		});
	}

	@Override
	public List<TreeNodeVo> listDb() {
		LambdaQueryWrapper<DataGovernanceMetadataEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.in(DataGovernanceMetadataEntity::getMetamodelId, BuiltInMetamodel.SCHEMA.getId(), BuiltInMetamodel.TABLE.getId())
				.or().isNull(DataGovernanceMetadataEntity::getMetamodelId)
				.orderByAsc(DataGovernanceMetadataEntity::getOrderNo);
		dataScopeWithOrgId(wrapper);
		List<DataGovernanceMetadataEntity> metadatas = baseMapper.selectList(wrapper);
		List<TreeNodeVo> treeNodeVos = BeanUtil.copyListProperties(metadatas, TreeNodeVo::new, (oldItem, newItem) -> {
			newItem.setLabel(oldItem.getName());
			newItem.setValue(oldItem.getId());
			newItem.setDisabled(!BuiltInMetamodel.TABLE.getId().equals(oldItem.getMetamodelId()));
			if (newItem.getPath().contains("/")) {
				newItem.setParentPath(newItem.getPath().substring(0, newItem.getPath().lastIndexOf("/")));
			}
		});
		return BuildTreeUtils.buildTree(treeNodeVos);
	}

	@Override
	public DataGovernanceMetadataVO get(Long id) {
		DataGovernanceMetadataEntity metadataEntity = getById(id);
		DataGovernanceMetadataVO metadataVO = DataGovernanceMetadataConvert.INSTANCE.convert(metadataEntity);
		//获取元数据的属性信息
		metadataVO.setProperties(metadataPropertyDao.listPropertyById(id, metadataEntity.getMetamodelId()));
		return metadataVO;
	}

	@Override
	public List<TreeNodeVo> listByKeyword(String keyword) {
		if (StringUtil.isBlank(keyword)) {
			return listByPatentId(0L);
		}
		LambdaQueryWrapper<DataGovernanceMetadataEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.like(DataGovernanceMetadataEntity::getName, keyword).or().like(DataGovernanceMetadataEntity::getCode, keyword).orderByAsc(DataGovernanceMetadataEntity::getOrderNo)
				.orderByAsc(DataGovernanceMetadataEntity::getId);
		dataScopeWithOrgId(wrapper);
		List<DataGovernanceMetadataEntity> metadatas = baseMapper.selectList(wrapper);
		List<DataGovernanceMetadataEntity> resultList = new ArrayList<>();
		//递归获取父级
		for (DataGovernanceMetadataEntity metadata : metadatas) {
			recursionAddParent(metadata, resultList);
		}
		List<DataGovernanceMetadataEntity> result = resultList.stream().sorted(Comparator.comparing(DataGovernanceMetadataEntity::getOrderNo)).collect(Collectors.toList());
		List<TreeNodeVo> treeNodeVos = BeanUtil.copyListProperties(result, TreeNodeVo::new, (oldItem, newItem) -> {
			newItem.setLabel(oldItem.getName());
			newItem.setValue(oldItem.getId());
			newItem.setLeaf(BuiltInMetamodel.COLUMN.getId().equals(oldItem.getMetamodelId()));
			if (newItem.getPath().contains("/")) {
				newItem.setParentPath(newItem.getPath().substring(0, newItem.getPath().lastIndexOf("/")));
			}
		});
		return BuildTreeUtils.buildTree(treeNodeVos);
	}

	@Override
	public List<TreeNodeVo> listFloder() {
		LambdaQueryWrapper<DataGovernanceMetadataEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataGovernanceMetadataEntity::getIfLeaf, 1).orderByAsc(DataGovernanceMetadataEntity::getOrderNo).orderByAsc(DataGovernanceMetadataEntity::getId);
		dataScopeWithOrgId(wrapper);
		List<DataGovernanceMetadataEntity> metadatas = baseMapper.selectList(wrapper);
		List<TreeNodeVo> treeNodeVos = BeanUtil.copyListProperties(metadatas, TreeNodeVo::new, (oldItem, newItem) -> {
			newItem.setLabel(oldItem.getName());
			newItem.setValue(oldItem.getId());
			if (newItem.getPath().contains("/")) {
				newItem.setParentPath(newItem.getPath().substring(0, newItem.getPath().lastIndexOf("/")));
			}
		});
		return BuildTreeUtils.buildTree(treeNodeVos);
	}

	@Override
	public void deleteAll(Long id) {
		LambdaQueryWrapper<DataGovernanceMetadataEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataGovernanceMetadataEntity::getParentId, id);
		List<DataGovernanceMetadataEntity> childs = baseMapper.selectList(wrapper);
		for (DataGovernanceMetadataEntity child : childs) {
			deleteAll(child.getId());
		}
		baseMapper.deleteById(id);
		LambdaQueryWrapper<DataGovernanceMetadataPropertyEntity> propertyWrapper = new LambdaQueryWrapper<>();
		propertyWrapper.eq(DataGovernanceMetadataPropertyEntity::getMetadataId, id);
		metadataPropertyDao.delete(propertyWrapper);
	}

	@Override
	public void upNeo4jInfo(Neo4jInfo neo4jInfo) {
		tokenStoreCache.saveNeo4jInfo(getProjectId(), neo4jInfo);
	}

	@Override
	public Neo4jInfo getNeo4jInfo() {
		return tokenStoreCache.getNeo4jInfo(getProjectId());
	}


	private void recursionAddParent(DataGovernanceMetadataEntity metadataEntity, List<DataGovernanceMetadataEntity> resultList) {
		if (resultList.stream().noneMatch(item -> metadataEntity.getId().equals(item.getId()))) {
			//添加自己
			resultList.add(metadataEntity);
		}
		//如果不是顶级
		if (metadataEntity.getParentId() != 0) {
			//获取父级，继续递归
			DataGovernanceMetadataEntity parent = getById(metadataEntity.getParentId());
			recursionAddParent(parent, resultList);
		}
	}
}
