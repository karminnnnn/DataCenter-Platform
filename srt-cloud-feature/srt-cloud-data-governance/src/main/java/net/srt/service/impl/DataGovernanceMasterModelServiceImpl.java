package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceMasterColumnConvert;
import net.srt.convert.DataGovernanceMasterModelConvert;
import net.srt.dao.DataGovernanceMasterColumnDao;
import net.srt.dao.DataGovernanceMasterDataCatalogDao;
import net.srt.dao.DataGovernanceMasterDistributeDao;
import net.srt.dao.DataGovernanceMasterDistributeLogDao;
import net.srt.dao.DataGovernanceMasterModelDao;
import net.srt.entity.DataGovernanceMasterColumnEntity;
import net.srt.entity.DataGovernanceMasterDataCatalogEntity;
import net.srt.entity.DataGovernanceMasterDistributeEntity;
import net.srt.entity.DataGovernanceMasterModelEntity;
import net.srt.framework.common.cache.bean.DataProjectCacheBean;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.framework.security.user.SecurityUser;
import net.srt.service.DataGovernanceMasterModelService;
import net.srt.vo.DataGovernanceMasterColumnVO;
import net.srt.vo.DataGovernanceMasterModelVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.type.ProductTypeEnum;
import srt.cloud.framework.dbswitch.core.model.ColumnDescription;
import srt.cloud.framework.dbswitch.core.service.IMetaDataByJdbcService;
import srt.cloud.framework.dbswitch.core.service.impl.MetaDataByJdbcServiceImpl;
import srt.cloud.framework.dbswitch.core.util.GenerateSqlUtils;
import srt.cloud.framework.dbswitch.data.entity.TargetDataSourceProperties;
import srt.cloud.framework.dbswitch.data.util.DataSourceUtils;
import srt.cloud.framework.dbswitch.dbcommon.database.DatabaseOperatorFactory;
import srt.cloud.framework.dbswitch.sql.ddl.sql.DdlSqlDropTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据治理-主数据模型
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-09-27
 */
@Service
@AllArgsConstructor
public class DataGovernanceMasterModelServiceImpl extends BaseServiceImpl<DataGovernanceMasterModelDao, DataGovernanceMasterModelEntity> implements DataGovernanceMasterModelService {

	private final DataGovernanceMasterColumnDao masterColumnDao;
	private final DataGovernanceMasterDistributeDao distributeDao;
	private final DataGovernanceMasterDataCatalogDao catalogDao;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public DataGovernanceMasterModelEntity save(DataGovernanceMasterModelVO vo) {
		//获取目录信息
		DataGovernanceMasterDataCatalogEntity catalogEntity = catalogDao.selectById(vo.getCatalogId());
		DataGovernanceMasterModelEntity entity = DataGovernanceMasterModelConvert.INSTANCE.convert(vo);
		entity.setOrgId(catalogEntity.getOrgId());
		Long projectId = getProjectId();
		entity.setProjectId(projectId);
		//判断是否存在该模型
		LambdaQueryWrapper<DataGovernanceMasterModelEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataGovernanceMasterModelEntity::getTableName, vo.getTableName()).eq(DataGovernanceMasterModelEntity::getProjectId, projectId).last("limit 1");
		DataGovernanceMasterModelEntity masterModelEntity = baseMapper.selectOne(wrapper);
		if (masterModelEntity != null) {
			throw new ServerException(String.format("名称为【%s】的主数据模型在该项目下已存在！", masterModelEntity.getTableName()));
		}
		baseMapper.insert(entity);
		addColumns(entity);
		return getByCatalogId(entity.getCatalogId());
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public DataGovernanceMasterModelEntity update(DataGovernanceMasterModelVO vo) {
		DataGovernanceMasterDataCatalogEntity catalogEntity = catalogDao.selectById(vo.getCatalogId());
		DataGovernanceMasterModelEntity entity = DataGovernanceMasterModelConvert.INSTANCE.convert(vo);
		entity.setOrgId(catalogEntity.getOrgId());
		Long projectId = getProjectId();
		entity.setProjectId(projectId);
		//判断是否存在该模型
		LambdaQueryWrapper<DataGovernanceMasterModelEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataGovernanceMasterModelEntity::getTableName, vo.getTableName()).eq(DataGovernanceMasterModelEntity::getProjectId, projectId).last("limit 1");
		DataGovernanceMasterModelEntity masterModelEntity = baseMapper.selectOne(wrapper);
		if (masterModelEntity != null && !masterModelEntity.getId().equals(vo.getId())) {
			throw new ServerException(String.format("名称为【%s】的主数据模型在该项目下已存在！", masterModelEntity.getTableName()));
		}
		updateById(entity);
		addColumns(entity);
		return getByCatalogId(entity.getCatalogId());
	}


	private void addColumns(DataGovernanceMasterModelEntity modelEntity) {
		//获取库中的字段列表
		LambdaQueryWrapper<DataGovernanceMasterColumnEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataGovernanceMasterColumnEntity::getMasterModelId, modelEntity.getId());
		List<DataGovernanceMasterColumnEntity> dbColumns = masterColumnDao.selectList(wrapper);
		List<DataGovernanceMasterColumnVO> columns = modelEntity.getColumns();
		//判断是否存在主键
		if (columns.stream().noneMatch(item -> item.getPk() == 1)) {
			throw new ServerException("主数据模型至少需要存在一个主键！");
		}
		//获取需要插入的数据
		List<DataGovernanceMasterColumnVO> toAdd = columns.stream().filter(item -> item.getId() == null).collect(Collectors.toList());
		for (DataGovernanceMasterColumnVO columnVO : toAdd) {
			columnVO.setMasterModelId(modelEntity.getId());
			columnVO.setProjectId(getProjectId());
			masterColumnDao.insert(DataGovernanceMasterColumnConvert.INSTANCE.convert(columnVO));
		}
		//获取需要更新的数据
		List<DataGovernanceMasterColumnVO> toUpdate = columns.stream().filter(item -> item.getId() != null).collect(Collectors.toList());
		for (DataGovernanceMasterColumnVO columnVO : toUpdate) {
			columnVO.setMasterModelId(modelEntity.getId());
			columnVO.setProjectId(getProjectId());
			masterColumnDao.updateById(DataGovernanceMasterColumnConvert.INSTANCE.convert(columnVO));
		}
		//获取需要删除的数据（库里有，传的没有）
		for (DataGovernanceMasterColumnEntity dbColumn : dbColumns) {
			if (columns.stream().noneMatch(item -> dbColumn.getId().equals(item.getId()))) {
				masterColumnDao.deleteById(dbColumn.getId());
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) {
		//删除字段
		LambdaQueryWrapper<DataGovernanceMasterColumnEntity> columnWrapper = Wrappers.lambdaQuery();
		columnWrapper.eq(DataGovernanceMasterColumnEntity::getMasterModelId, id);
		masterColumnDao.delete(columnWrapper);
		removeById(id);
	}

	@Override
	public DataGovernanceMasterModelEntity getByCatalogId(Long catalogId) {
		LambdaQueryWrapper<DataGovernanceMasterModelEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataGovernanceMasterModelEntity::getCatalogId, catalogId).last("limit 1");
		DataGovernanceMasterModelEntity modelEntity = baseMapper.selectOne(wrapper);
		if (modelEntity != null) {
			LambdaQueryWrapper<DataGovernanceMasterColumnEntity> columnWrapper = Wrappers.lambdaQuery();
			columnWrapper.eq(DataGovernanceMasterColumnEntity::getMasterModelId, modelEntity.getId());
			List<DataGovernanceMasterColumnEntity> columnEntities = masterColumnDao.selectList(columnWrapper);
			modelEntity.setColumns(DataGovernanceMasterColumnConvert.INSTANCE.convertList(columnEntities));
			return modelEntity;
		} else {
			return null;
		}
	}

	@Override
	public DataGovernanceMasterModelEntity release(Long id) {
		DataGovernanceMasterModelEntity masterModelEntity = baseMapper.selectById(id);
		masterModelEntity.setStatus(1);
		masterModelEntity.setReleaseTime(new Date());
		masterModelEntity.setReleaseUserId(SecurityUser.getUserId());
		LambdaQueryWrapper<DataGovernanceMasterColumnEntity> columnWrapper = Wrappers.lambdaQuery();
		columnWrapper.eq(DataGovernanceMasterColumnEntity::getMasterModelId, id);
		List<DataGovernanceMasterColumnEntity> columnEntities = masterColumnDao.selectList(columnWrapper);
		List<ColumnDescription> columnDescriptions = new ArrayList<>();
		List<String> columnPkDescriptions = new ArrayList<>();
		for (DataGovernanceMasterColumnEntity columnEntity : columnEntities) {
			ColumnDescription columnDescription = DataGovernanceMasterColumnEntity.makeColumnDescription(columnEntity);
			columnDescriptions.add(columnDescription);
			if (columnDescription.isPk()) {
				columnPkDescriptions.add(columnDescription.getFieldName());
			}
		}
		DataProjectCacheBean project = getProject();
		ProductTypeEnum productTypeEnum = ProductTypeEnum.getByIndex(project.getDbType());
		List<String> ddlCreateTableSQL = GenerateSqlUtils.getDDLCreateTableSQL(productTypeEnum, columnDescriptions, columnPkDescriptions, project.getDbSchema(), masterModelEntity.getTableName(), masterModelEntity.getTableCn(), false, null);
		IMetaDataByJdbcService metaDataByJdbcService = new MetaDataByJdbcServiceImpl(productTypeEnum);
		for (String sql : ddlCreateTableSQL) {
			metaDataByJdbcService.executeSql(project.getDbUrl(), project.getDbUsername(), project.getDbPassword(), sql);
		}
		//更新表状态
		baseMapper.updateById(masterModelEntity);
		return baseMapper.selectById(id);
	}

	@Override
	public DataGovernanceMasterModelEntity cancelRelease(Long id) {
		//判断是否有数据派发任务与之关联
		LambdaQueryWrapper<DataGovernanceMasterDistributeEntity> distributeWrapper = Wrappers.lambdaQuery();
		distributeWrapper.eq(DataGovernanceMasterDistributeEntity::getMasterModelId, id).last("LIMIT 1");
		DataGovernanceMasterDistributeEntity distributeEntity = distributeDao.selectOne(distributeWrapper);
		if (distributeEntity != null) {
			throw new ServerException(String.format("存在主数据派发任务【%s】与之关联，暂不可取消发布！", distributeEntity.getName()));
		}
		DataGovernanceMasterModelEntity masterModelEntity = baseMapper.selectById(id);
		masterModelEntity.setStatus(0);
		masterModelEntity.setReleaseTime(null);
		masterModelEntity.setReleaseUserId(null);
		DataProjectCacheBean project = getProject();
		ProductTypeEnum productTypeEnum = ProductTypeEnum.getByIndex(project.getDbType());
		TargetDataSourceProperties properties = new TargetDataSourceProperties();
		properties.setUrl(project.getDbUrl());
		properties.setDriverClassName(productTypeEnum.getDriveClassName());
		properties.setTargetProductType(productTypeEnum);
		properties.setUsername(project.getDbUsername());
		properties.setPassword(project.getDbPassword());
		//清除表
		try (HikariDataSource targetDataSource = DataSourceUtils.createTargetDataSource(properties)) {
			DatabaseOperatorFactory.createDatabaseOperator(targetDataSource, productTypeEnum)
					.dropTable(project.getDbSchema(), masterModelEntity.getTableName());
		}
		baseMapper.updateById(masterModelEntity);
		return baseMapper.selectById(id);
	}

}
