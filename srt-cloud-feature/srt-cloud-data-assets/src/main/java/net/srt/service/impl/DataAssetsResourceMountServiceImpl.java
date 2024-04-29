package net.srt.service.impl;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.TablesNamesFinder;
import net.srt.api.module.data.governance.DataMetadataApi;
import net.srt.api.module.data.governance.constant.DbType;
import net.srt.api.module.data.governance.dto.DataGovernanceMetadataDto;
import net.srt.api.module.data.integrate.DataDatabaseApi;
import net.srt.api.module.data.integrate.DataFileApi;
import net.srt.api.module.data.integrate.dto.DataDatabaseDto;
import net.srt.api.module.data.integrate.dto.DataFileDto;
import net.srt.api.module.data.service.DataServiceApi;
import net.srt.api.module.data.service.dto.DataServiceApiConfigDto;
import net.srt.constant.DataHouseLayer;
import net.srt.constant.MiddleTreeNodeType;
import net.srt.constant.ResourceMountType;
import net.srt.convert.DataAssetsResourceMountConvert;
import net.srt.dao.DataAssetsResourceDao;
import net.srt.dao.DataAssetsResourceMountDao;
import net.srt.dto.SqlCheckResultDto;
import net.srt.dto.SqlExecuteDto;
import net.srt.entity.DataAssetsResourceEntity;
import net.srt.entity.DataAssetsResourceMountEntity;
import net.srt.framework.common.cache.bean.DataProjectCacheBean;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.framework.security.user.SecurityUser;
import net.srt.query.DataAssetsResourceMountQuery;
import net.srt.service.DataAssetsResourceMountService;
import net.srt.vo.DataAssetsResourceMountVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.type.ProductTypeEnum;
import srt.cloud.framework.dbswitch.common.util.StringUtil;
import srt.cloud.framework.dbswitch.core.model.TableDescription;
import srt.cloud.framework.dbswitch.core.service.IMetaDataByJdbcService;
import srt.cloud.framework.dbswitch.core.service.impl.MetaDataByJdbcServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据资产-资源挂载表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-07-06
 */
@Service
@AllArgsConstructor
public class DataAssetsResourceMountServiceImpl extends BaseServiceImpl<DataAssetsResourceMountDao, DataAssetsResourceMountEntity> implements DataAssetsResourceMountService {

	private final DataMetadataApi dataMetadataApi;
	private final DataDatabaseApi dataDatabaseApi;
	private final DataServiceApi dataServiceApi;
	private final DataFileApi dataFileApi;
	private final DataAssetsResourceDao dataAssetsResourceDao;

	@Override
	public PageResult<DataAssetsResourceMountVO> page(DataAssetsResourceMountQuery query) {
		IPage<DataAssetsResourceMountEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataAssetsResourceMountConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	private LambdaQueryWrapper<DataAssetsResourceMountEntity> getWrapper(DataAssetsResourceMountQuery query) {
		LambdaQueryWrapper<DataAssetsResourceMountEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataAssetsResourceMountEntity::getResourceId, query.getResourceId())
				.like(StringUtil.isNotBlank(query.getMountName()), DataAssetsResourceMountEntity::getMountName, query.getMountName())
				.orderByDesc(DataAssetsResourceMountEntity::getUpdateTime).orderByDesc(DataAssetsResourceMountEntity::getId);
		return wrapper;
	}

	@Override
	public void save(DataAssetsResourceMountVO vo) {
		DataAssetsResourceMountEntity entity = DataAssetsResourceMountConvert.INSTANCE.convert(vo);
		//判断是否资源已挂载
		LambdaQueryWrapper<DataAssetsResourceMountEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataAssetsResourceMountEntity::getResourceId, vo.getResourceId()).eq(DataAssetsResourceMountEntity::getMountType, vo.getMountType()).eq(DataAssetsResourceMountEntity::getMountId, vo.getMountId()).last("limit 1");
		DataAssetsResourceMountEntity resourceMountEntity = baseMapper.selectOne(wrapper);
		entity.setProjectId(getProjectId());
		entity.setCheckLog("状态正常");
		if (resourceMountEntity != null) {
			entity.setId(resourceMountEntity.getId());
			updateById(entity);
			return;
		}
		baseMapper.insert(entity);
		//更新资源为已挂载
		DataAssetsResourceEntity resourceEntity = dataAssetsResourceDao.selectById(vo.getResourceId());
		resourceEntity.setMountStatus(1);
		dataAssetsResourceDao.updateById(resourceEntity);
	}

	@Override
	public void update(DataAssetsResourceMountVO vo) {
		DataAssetsResourceMountEntity entity = DataAssetsResourceMountConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) {
		DataAssetsResourceMountEntity resourceMountEntity = baseMapper.selectById(id);
		removeById(id);
		//查询是否还有没有挂载的资源
		LambdaQueryWrapper<DataAssetsResourceMountEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataAssetsResourceMountEntity::getResourceId, resourceMountEntity.getResourceId()).last("limit 1");
		DataAssetsResourceMountEntity one = baseMapper.selectOne(wrapper);
		if (one == null) {
			//更新资源为未挂载
			DataAssetsResourceEntity resourceEntity = dataAssetsResourceDao.selectById(resourceMountEntity.getResourceId());
			resourceEntity.setMountStatus(0);
			dataAssetsResourceDao.updateById(resourceEntity);
		}
	}

	@Override
	public void check(Long resourceId) {
		LambdaQueryWrapper<DataAssetsResourceMountEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataAssetsResourceMountEntity::getResourceId, resourceId).orderByDesc(DataAssetsResourceMountEntity::getId);
		List<DataAssetsResourceMountEntity> resourceMountEntities = baseMapper.selectList(wrapper);
		for (DataAssetsResourceMountEntity resourceMountEntity : resourceMountEntities) {
			Long mountId = resourceMountEntity.getMountId();
			if (ResourceMountType.DATABASE.getValue().equals(resourceMountEntity.getMountType())) {
				//查询元数据
				DataGovernanceMetadataDto metadataDto = dataMetadataApi.getById(mountId.intValue()).getData();
				if (metadataDto == null) {
					resourceMountEntity.setStatus(0);
					resourceMountEntity.setCheckLog("没有查询到对应的元数据信息，元数据可能已被删除");
				} else {
					if (DbType.DATABASE.getValue().equals(metadataDto.getDbType())) {
						Long datasourceId = metadataDto.getDatasourceId();
						DataDatabaseDto databaseDto = dataDatabaseApi.getById(datasourceId).getData();
						if (databaseDto == null) {
							resourceMountEntity.setStatus(0);
							resourceMountEntity.setCheckLog("存在元数据信息，但根据元数据信息无法查询到对应的数据库信息，数据库信息可能已被删除");
						} else {
							ProductTypeEnum productTypeEnum = ProductTypeEnum.getByIndex(databaseDto.getDatabaseType());
							IMetaDataByJdbcService metaDataService = new MetaDataByJdbcServiceImpl(productTypeEnum);
							List<TableDescription> tableDescriptions = metaDataService.queryTableList(databaseDto.getJdbcUrl(), databaseDto.getUserName(), databaseDto.getPassword(), databaseDto.getDatabaseSchema()
							);
							boolean hasTable = tableDescriptions.stream().anyMatch(item -> item.getTableName().equals(metadataDto.getCode()));
							if (!hasTable) {
								resourceMountEntity.setStatus(0);
								resourceMountEntity.setCheckLog("存在元数据信息，但根据元数据信息连接数据库查询不到对应的物理表，元数据信息可能已不是最新，请及时更新");
							} else {
								resourceMountEntity.setStatus(1);
								resourceMountEntity.setCheckLog("状态正常");
							}
						}
					} else {
						DataProjectCacheBean project = getProject();
						IMetaDataByJdbcService metaDataService = new MetaDataByJdbcServiceImpl(ProductTypeEnum.getByIndex(project.getDbType()));
						List<TableDescription> tableList = metaDataService.queryTableList(project.getDbUrl(), project.getDbUsername(), project.getDbPassword(), project.getDbSchema());
						boolean hasTable = tableList.stream().anyMatch(item -> item.getTableName().equals(metadataDto.getCode()));
						if (!hasTable) {
							resourceMountEntity.setStatus(0);
							resourceMountEntity.setCheckLog("存在元数据信息，但根据元数据信息连接数据库查询不到对应的物理表，元数据信息可能已不是最新，请及时更新");
						} else {
							resourceMountEntity.setStatus(1);
							resourceMountEntity.setCheckLog("状态正常");
						}
					}
				}
			} else if (ResourceMountType.API.getValue().equals(resourceMountEntity.getMountType())) {
				DataServiceApiConfigDto apiConfigDto = dataServiceApi.getById(mountId).getData();
				if (apiConfigDto == null) {
					resourceMountEntity.setStatus(0);
					resourceMountEntity.setCheckLog("未查询到挂载的 API 资源，可能已被删除");
				} else {
					resourceMountEntity.setStatus(1);
					resourceMountEntity.setCheckLog("状态正常");
				}
			} else if (ResourceMountType.FILE.getValue().equals(resourceMountEntity.getMountType())) {
				DataFileDto dataFileDto = dataFileApi.getById(mountId).getData();
				if (dataFileDto == null) {
					resourceMountEntity.setStatus(0);
					resourceMountEntity.setCheckLog("未查询到挂载的文件资源，可能已被删除");
				} else {
					resourceMountEntity.setStatus(1);
					resourceMountEntity.setCheckLog("状态正常");
				}
			}
			updateById(resourceMountEntity);
		}
	}

	@Override
	public Map<DbType, List<TreeNodeVo>> getDbInfo(Long id, Integer queryApply) {
		Map<DbType, List<TreeNodeVo>> resultMap = new HashMap<>();
		List<DataAssetsResourceMountEntity> mountEntities;
		if (queryApply == 0) {
			//获取挂载的库表信息
			LambdaQueryWrapper<DataAssetsResourceMountEntity> wrapper = Wrappers.lambdaQuery();
			wrapper.eq(DataAssetsResourceMountEntity::getResourceId, id).eq(DataAssetsResourceMountEntity::getMountType, ResourceMountType.DATABASE.getValue()).orderByDesc(DataAssetsResourceMountEntity::getId);
			mountEntities = baseMapper.selectList(wrapper);
		} else {
			//获取申请的挂载资源
			mountEntities = baseMapper.selectDbApplyActiveList(id, SecurityUser.getUserId());
		}

		Map<Long, List<DataGovernanceMetadataDto>> dbMap = new LinkedHashMap<>();
		for (DataAssetsResourceMountEntity mountEntity : mountEntities) {
			Long mountId = mountEntity.getMountId();
			//都是表
			DataGovernanceMetadataDto metadataDto = dataMetadataApi.getById(mountId.intValue()).getData();
			if (metadataDto != null) {
				List<DataGovernanceMetadataDto> tables = new ArrayList<>(1);
				Long parentId = metadataDto.getParentId();
				if (dbMap.containsKey(parentId)) {
					tables = dbMap.get(parentId);
				} else {
					dbMap.put(parentId, tables);
				}
				tables.add(metadataDto);
			}
		}
		List<TreeNodeVo> dbTreeNodeVos = new ArrayList<>();
		List<TreeNodeVo> middleDbTreeNodeVos = new ArrayList<>(1);
		for (Map.Entry<Long, List<DataGovernanceMetadataDto>> dbEntry : dbMap.entrySet()) {
			Long dbId = dbEntry.getKey();
			List<DataGovernanceMetadataDto> tables = dbEntry.getValue();
			DataGovernanceMetadataDto db = dataMetadataApi.getById(dbId.intValue()).getData();
			if (db != null) {
				if (DbType.DATABASE.getValue().equals(db.getDbType())) {
					//数据库
					Long datasourceId = db.getDatasourceId();
					DataDatabaseDto databaseDto = dataDatabaseApi.getById(datasourceId).getData();
					if (databaseDto != null) {
						//获取表列表
						ProductTypeEnum productTypeEnum = ProductTypeEnum.getByIndex(databaseDto.getDatabaseType());
						IMetaDataByJdbcService metaDataService = new MetaDataByJdbcServiceImpl(productTypeEnum);
						List<TableDescription> tableDescriptions = metaDataService.queryTableList(databaseDto.getJdbcUrl(), databaseDto.getUserName(), databaseDto.getPassword(),
								databaseDto.getDatabaseSchema());
						TreeNodeVo dbNode = new TreeNodeVo();
						dbTreeNodeVos.add(dbNode);
						dbNode.setName(databaseDto.getName());
						dbNode.setDescription(databaseDto.getName());
						dbNode.setLabel(databaseDto.getDatabaseName());
						dbNode.setId(databaseDto.getId());
						dbNode.setIfLeaf(1);
						dbNode.setAttributes(databaseDto);
						List<TreeNodeVo> tableNodes = new ArrayList<>(10);
						dbNode.setChildren(tableNodes);
						for (TableDescription table : tableDescriptions) {
							TreeNodeVo tableNode = new TreeNodeVo();
							tableNode.setParentId(databaseDto.getId());
							tableNode.setLabel(table.getTableName());
							tableNode.setName(table.getTableName());
							tableNode.setDescription(table.getRemarks());
							tableNode.setIfLeaf(0);
							for (DataGovernanceMetadataDto metadataDto : tables) {
								if (metadataDto.getCode().equals(table.getTableName())) {
									tableNodes.add(tableNode);
								}
							}
						}
					}
				} else {
					DataDatabaseDto dataDatabaseDto = new DataDatabaseDto();
					DataProjectCacheBean project = getProject();
					dataDatabaseDto.setDatabaseName(project.getDbName());
					dataDatabaseDto.setDatabaseSchema(project.getDbSchema());
					dataDatabaseDto.setJdbcUrl(project.getDbUrl());
					dataDatabaseDto.setUserName(project.getDbUsername());
					dataDatabaseDto.setPassword(project.getDbPassword());
					dataDatabaseDto.setName(project.getName() + "<中台库>");
					TreeNodeVo dbNode = new TreeNodeVo();
					middleDbTreeNodeVos.add(dbNode);
					dbNode.setCode("middle_" + project.getId());
					dbNode.setIfLeaf(1);
					dbNode.setName(dataDatabaseDto.getDatabaseName());
					dbNode.setLabel(dataDatabaseDto.getDatabaseName());
					dbNode.setDescription(dataDatabaseDto.getName());
					dbNode.setAttributes(dataDatabaseDto);
					dbNode.setType(MiddleTreeNodeType.DB.getValue());
					List<TreeNodeVo> layerList = new ArrayList<>(1);
					dbNode.setChildren(layerList);
					//获取该项目下的所有表
					IMetaDataByJdbcService metaDataService = new MetaDataByJdbcServiceImpl(ProductTypeEnum.getByIndex(project.getDbType()));
					List<TableDescription> tableList = metaDataService.queryTableList(dataDatabaseDto.getJdbcUrl(), dataDatabaseDto.getUserName(), dataDatabaseDto.getPassword(), dataDatabaseDto.getDatabaseSchema());
					for (DataHouseLayer layer : DataHouseLayer.values()) {
						TreeNodeVo layerNode = new TreeNodeVo();
						layerNode.setIfLeaf(1);
						layerNode.setName(layer.name());
						layerNode.setLabel(layer.name());
						layerNode.setDescription(layer.getName());
						layerNode.setType(MiddleTreeNodeType.LAYER.getValue());
						layerList.add(layerNode);
						List<TreeNodeVo> tableNodeList = tableList.stream().filter(
								table -> tables.stream().anyMatch(metaTable -> table.getTableName().equals(metaTable.getCode())) && table.getTableName().startsWith(layer.getTablePrefix()) && !DataHouseLayer.OTHER.equals(layer)
										|| DataHouseLayer.OTHER.equals(layer)
										&& !table.getTableName().startsWith(DataHouseLayer.ODS.getTablePrefix())
										&& !table.getTableName().startsWith(DataHouseLayer.DIM.getTablePrefix())
										&& !table.getTableName().startsWith(DataHouseLayer.DWD.getTablePrefix())
										&& !table.getTableName().startsWith(DataHouseLayer.DWS.getTablePrefix())
										&& !table.getTableName().startsWith(DataHouseLayer.ADS.getTablePrefix())).map(table -> {
							TreeNodeVo nodeVo = new TreeNodeVo();
							nodeVo.setIfLeaf(0);
							nodeVo.setName(table.getTableName());
							nodeVo.setLabel(table.getTableName());
							nodeVo.setDescription(table.getRemarks());
							nodeVo.setType(MiddleTreeNodeType.TABLE.getValue());
							return nodeVo;
						}).collect(Collectors.toList());
						layerNode.setChildren(tableNodeList);
					}
				}
			}
		}
		resultMap.put(DbType.DATABASE, dbTreeNodeVos);
		resultMap.put(DbType.MIDDLE_DB, middleDbTreeNodeVos);
		return resultMap;
	}

	@SneakyThrows
	@Override
	public SqlCheckResultDto sqlCheck(SqlExecuteDto sqlExecuteDto) {
		LambdaQueryWrapper<DataAssetsResourceMountEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataAssetsResourceMountEntity::getResourceId, sqlExecuteDto.getResourceId()).eq(DataAssetsResourceMountEntity::getMountType, ResourceMountType.DATABASE.getValue()).orderByDesc(DataAssetsResourceMountEntity::getId);
		List<DataAssetsResourceMountEntity> mountEntities = baseMapper.selectList(wrapper);
		List<DataGovernanceMetadataDto> metadataDtos = new ArrayList<>(10);
		for (DataAssetsResourceMountEntity mountEntity : mountEntities) {
			Long mountId = mountEntity.getMountId();
			//都是表
			DataGovernanceMetadataDto metadataDto = dataMetadataApi.getById(mountId.intValue()).getData();
			if (metadataDto != null) {
				metadataDtos.add(metadataDto);
			}
		}
		List<DataGovernanceMetadataDto> authTables;
		Long databaseId = sqlExecuteDto.getDatabaseId();
		ProductTypeEnum productTypeEnum;
		if (databaseId != null) {
			DataDatabaseDto databaseDto = dataDatabaseApi.getById(databaseId).getData();
			productTypeEnum = ProductTypeEnum.getByIndex(databaseDto.getDatabaseType());
			authTables = metadataDtos.stream().filter(item -> item.getDatasourceId().equals(databaseId)).collect(Collectors.toList());
		} else {
			productTypeEnum = ProductTypeEnum.getByIndex(getProject().getDbType());
			authTables = metadataDtos.stream().filter(item -> item.getDatasourceId() == -1).collect(Collectors.toList());
		}
		List<String> tableNames = new ArrayList<>(10);
		//解析sql
		List<SQLStatement> stmtList = SQLUtils.parseStatements(sqlExecuteDto.getStatement(), productTypeEnum.name().toLowerCase());
		for (SQLStatement item : stmtList) {
			String sql = item.toString();
			//获取sql中的表
			Statement statement = CCJSqlParserUtil.parse(sql);
			TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
			List<String> tableList = tablesNamesFinder.getTableList(statement);
			for (String table : tableList) {
				String[] tableArr = table.split("\\.");
				table = tableArr.length > 1 ? tableArr[1] : table;
				tableNames.add(table.toLowerCase());
			}
		}
		List<String> authTableNames = new ArrayList<>();
		//判断有没有操作授权表之外的表
		for (DataGovernanceMetadataDto authTable : authTables) {
			String authTableName = authTable.getCode();
			authTableNames.add(authTableName.toLowerCase());
		}
		List<String> noAuthTables = new ArrayList<>();
		for (String tableName : tableNames) {
			if (!authTableNames.contains(tableName)) {
				noAuthTables.add(tableName);
			}
		}
		SqlCheckResultDto sqlCheckResultDto = new SqlCheckResultDto();
		if (noAuthTables.isEmpty()) {
			sqlCheckResultDto.setPass(true);
		} else {
			sqlCheckResultDto.setPass(false);
			sqlCheckResultDto.setMsg(String.format("您没有权限操作表 [%s]", String.join(",", noAuthTables)));
		}
		return sqlCheckResultDto;
	}

}
