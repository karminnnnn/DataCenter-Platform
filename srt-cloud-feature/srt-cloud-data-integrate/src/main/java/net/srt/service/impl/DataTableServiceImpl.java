package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.api.DataAccessApiImpl;
import net.srt.api.DataDatabaseApiImpl;
import net.srt.api.module.data.integrate.dto.DataAccessDto;
import net.srt.api.module.data.integrate.dto.DataSourceDto;
import net.srt.api.module.quartz.QuartzDataAccessApi;
import net.srt.constants.DataHouseLayer;
import net.srt.dao.DataTableDao;
import net.srt.entity.DataTableEntity;
import net.srt.framework.common.cache.bean.DataProjectCacheBean;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.SqlUtils;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataFieldQuery;
import net.srt.query.DataTableQuery;
import net.srt.query.TableDataQuery;
import net.srt.query.UpdateDataQuery;
import net.srt.service.DataTableService;
import net.srt.vo.ColumnDescriptionVo;
import net.srt.vo.DataTableVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.type.ProductTypeEnum;
import srt.cloud.framework.dbswitch.common.util.StringUtil;
import srt.cloud.framework.dbswitch.core.model.ColumnDescription;
import srt.cloud.framework.dbswitch.core.model.SchemaTableData;
import srt.cloud.framework.dbswitch.core.model.TableDescription;
import srt.cloud.framework.dbswitch.core.service.IMetaDataByJdbcService;
import srt.cloud.framework.dbswitch.core.service.impl.MetaDataByJdbcServiceImpl;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据集成-贴源数据
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-11-07
 */
@Service
@AllArgsConstructor
public class DataTableServiceImpl extends BaseServiceImpl<DataTableDao, DataTableEntity> implements DataTableService {
	private DataAccessApiImpl dataAccessApi;
	private DataDatabaseApiImpl dataDatabaseApi;
	private final QuartzDataAccessApi quartzDataAccessApi;

	@Override
	public PageResult<DataTableVO> page(DataTableQuery query) {
		/*IPage<DataOdsEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));
		return new PageResult<>(DataOdsConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());*/
		//获取该项目下的所有表
		DataProjectCacheBean project = getProject();
		IMetaDataByJdbcService metaDataService = new MetaDataByJdbcServiceImpl(ProductTypeEnum.getByIndex(project.getDbType()));
		List<TableDescription> tableList = metaDataService.queryTableList(project.getDbUrl(), project.getDbUsername(), project.getDbPassword(), project.getDbSchema())
				.stream().filter(item -> item.getTableName().startsWith(DataHouseLayer.ODS.getTablePrefix()))
				.filter(item -> !StringUtil.isNotBlank(query.getDatatableName()) || item.getTableName().contains(query.getDatatableName()))
				.filter(item -> !StringUtil.isNotBlank(query.getRemarks()) || item.getRemarks().contains(query.getRemarks()))
				.collect(Collectors.toList());
		int startIndex = (query.getPage() - 1) * query.getLimit();
		int endIndex = Math.min(query.getPage() * query.getLimit(), tableList.size());
		List<TableDescription> pageList = tableList.subList(startIndex, endIndex);//截取片段tabledescription
		List<DataTableVO> dataTableVOS = new ArrayList<>(10);//创建了datatbleVO
		for (TableDescription tableDescription : pageList) {//遍历pagelist转化为datatablevo
			DataTableVO dataTableVO = new DataTableVO();
			dataTableVO.setDatatableName(tableDescription.getTableName());
			dataTableVO.setRemarks(tableDescription.getRemarks());
			dataTableVO.setProjectId(project.getId());
			//查询data_ods,补充数据
			LambdaQueryWrapper<DataTableEntity> wrapper = Wrappers.lambdaQuery();
			wrapper.eq(DataTableEntity::getTableName, dataTableVO.getDatatableName()).eq(DataTableEntity::getProjectId, dataTableVO.getProjectId()).last("LIMIT 1");
			DataTableEntity dataTableEntity = baseMapper.selectOne(wrapper);
			if (dataTableEntity != null) {
				//说明是通过数据接入接入的
				String datatablename=dataTableVO.getDatatableName();
				datatablename=datatablename.replace("ods_","");
				dataTableVO.setDatatableName(datatablename);
				dataTableVO.setDataAccessId(dataTableEntity.getDataAccessId());
				dataTableVO.setDatatableId(dataTableEntity.getId());
				DataAccessDto dataAccessDto=dataAccessApi.getById(dataTableEntity.getDataAccessId()).getData();
				Long databaseid=dataAccessDto.getSourceDatabaseId();
				dataTableVO.setDatabaseId(databaseid);
				dataTableVO.setDatabaseName(dataDatabaseApi.getDataBaseBamebyId(databaseid).getData());
				dataTableVO.setRecentlySyncTime(dataTableEntity.getRecentlySyncTime());
			}
			dataTableVOS.add(dataTableVO);
		}
		// 进行额外的过滤操作，过滤出符合数据库 ID 的表
		//List<DataTableVO> filteredDataTableVOS = dataTableVOS.stream()
	//			.filter(dataTableVO -> dataTableVO.getDatabaseId().equals(query.getDatabaseId()))
	//			.collect(Collectors.toList());

		return new PageResult<>(dataTableVOS, tableList.size());
	}

	/*private LambdaQueryWrapper<DataOdsEntity> getWrapper(DataOdsQuery query) {
		LambdaQueryWrapper<DataOdsEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StringUtil.isNotBlank(query.getTableName()), DataOdsEntity::getTableName, query.getTableName());
		wrapper.like(StringUtil.isNotBlank(query.getRemarks()), DataOdsEntity::getRemarks, query.getRemarks());
		wrapper.eq(query.getProjectId() != null, DataOdsEntity::getProjectId, query.getProjectId());
		dataScopeWithoutOrgId(wrapper);
		return wrapper;
	}*/

	@Override
	public void save(DataTableVO vo) {
		//DataTableEntity entity = DataOdsConvert.INSTANCE.convert(vo);
		//baseMapper.insert(entity);
		newDataTable(vo);
		quartzDataAccessApi.handRun(dataAccessApi.getAccessIDbydatabaseID(vo.getDatabaseId()));
	}

	@Override
	public void update(DataTableVO vo) {
		String Newtablename=vo.getDatatableName();
		DataTableEntity entity=baseMapper.selectById(vo.getDatatableId());
		String OldtableName=entity.getTableName().replace("ods_","");
		modifyDataTable(vo);
		log.error(Newtablename);
		log.error(OldtableName);
		if(!OldtableName.equals(Newtablename))
		{  log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!f23g1");
			deleteODSDatabaseTable(vo.getDatatableId());}
		quartzDataAccessApi.handRun(vo.getDataAccessId());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		for (Long id : idList) {
			deleteSourceDatabaseTable(id);//删除源数据库
			deleteODSDatabaseTable(id);
		}

	}

	@Override
	public DataTableEntity getByTableName(Long projectId, String tableName) {
		LambdaQueryWrapper<DataTableEntity> wrapper = new LambdaQueryWrapper<>();
		return baseMapper.selectOne(wrapper.eq(DataTableEntity::getTableName, tableName).eq(DataTableEntity::getProjectId, projectId));
	}

	/*@Override
	public SchemaTableDataVo getTableData(String tableName) {
		DataProjectCacheBean project = getProject();
		IMetaDataByJdbcService service = new MetaDataByJdbcServiceImpl(ProductTypeEnum.getByIndex(project.getDbType()));
		SchemaTableData schemaTableData = service.queryTableData(project.getDbUrl(), project.getDbUsername(), project.getDbPassword(), project.getDbSchema(), tableName, 50);
		return SchemaTableDataVo.builder().columns(SqlUtils.convertColumns(schemaTableData.getColumns())).rows(SqlUtils.convertRows(schemaTableData.getColumns(), schemaTableData.getRows())).build();
	}*/

	@Override
	public boolean saveTableData(UpdateDataQuery query) {
		// 获取数据接入信息
		DataAccessDto dataAccess = dataAccessApi.getById(getaccessidbydatabaseid(query.getDatatableId())).getData();
		if (dataAccess == null) {
			throw new RuntimeException("Data access not found");
		}

		// 获取源端数据库信息
		Long datasourceid = dataDatabaseApi.getDatasourceIdbyDatabaseId(dataAccess.getSourceDatabaseId()).getData();
		DataSourceDto dataSource = dataDatabaseApi.getById(datasourceid).getData();
		if (dataSource == null) {
			throw new RuntimeException("Data Source not found");
		}

		// 修改源端数据库中的表数据
		boolean ifsuccess=insertMultipleToSourceDatabaseTableData(dataSource, query);
		if(ifsuccess) {
			Long accessid=baseMapper.selectById(query.getDatatableId()).getDataAccessId();
			quartzDataAccessApi.handRun(accessid);
		}
		return ifsuccess;
	}

	public boolean deleteTableData(List<Map<String, Object>> List,Long datatableId ){
		// 获取数据接入信息
		DataAccessDto dataAccess = dataAccessApi.getById(getaccessidbydatabaseid(datatableId)).getData();
		if (dataAccess == null) {
			throw new RuntimeException("Data access not found");
		}

		// 获取源端数据库信息
		Long datasourceid = dataDatabaseApi.getDatasourceIdbyDatabaseId(dataAccess.getSourceDatabaseId()).getData();
		DataSourceDto dataSource = dataDatabaseApi.getById(datasourceid).getData();
		if (dataSource == null) {
			throw new RuntimeException("Data Source not found");
		}

		List<String> primaryKeyColumn=getPrimaryKeyColumn(datatableId);
		List<List<Object>> primaryKeyValueList=getPrimaryKeyValue(List,primaryKeyColumn);

		// 删除源端数据库中的表数据
		boolean ifsuccess=deleteMultipleFromSourceDatabaseTableData(dataSource,datatableId,primaryKeyValueList,primaryKeyColumn);
		if(ifsuccess) {
			Long accessid=baseMapper.selectById(datatableId).getDataAccessId();
			quartzDataAccessApi.handRun(accessid);
		}
		return ifsuccess;
	}

	private List<List<Object>> getPrimaryKeyValue(List<Map<String, Object>> list, List<String> primaryKeyColumn) {
		List<List<Object>> PrimaryKeyValue=new ArrayList<>();
		for (Map<String, Object> row : list) {
			List<Object> primaryKeyRow = new ArrayList<>();
			for (String key : primaryKeyColumn) {
				primaryKeyRow.add(row.get(key));
			}
			PrimaryKeyValue.add(primaryKeyRow);
		}
		return PrimaryKeyValue;
	}

	/*public SchemaTableDataVo pageTableData(TableDataQuery query){
		String tableName=baseMapper.selectById(query.getDatatableId()).getTableName();
		DataProjectCacheBean project = getProject();//获取项目编号
		IMetaDataByJdbcService service = new MetaDataByJdbcServiceImpl(ProductTypeEnum.getByIndex(project.getDbType()));
		SchemaTableData schemaTableData = service.queryTableData(project.getDbUrl(), project.getDbUsername(), project.getDbPassword(), project.getDbSchema(), tableName, 50);//初始化元数据服务,总之得到service对象
		return SchemaTableDataVo.builder().columns(SqlUtils.convertColumns(schemaTableData.getColumns())).rows(SqlUtils.convertRows(schemaTableData.getColumns(), schemaTableData.getRows())).build();//调用 queryTableData 方法查询指定表的数据。 参数包括数据库 URL、用户名、密码、模式、表名和查询限制（50 行）。查询结果存储在 schemaTableData 对象中。
	}*/

	@Override
	public PageResult<Map<String, Object>> pageTableData(TableDataQuery query) {
		String tableName=baseMapper.selectById(query.getDatatableId()).getTableName();
		DataProjectCacheBean project = getProject();
		IMetaDataByJdbcService service = new MetaDataByJdbcServiceImpl(ProductTypeEnum.getByIndex(project.getDbType()));

		// 设置合理的最大行数限制
		int maxRows = 50000000;
		// 查询所有表数据
		SchemaTableData schemaTableData = service.queryTableData(project.getDbUrl(), project.getDbUsername(), project.getDbPassword(), project.getDbSchema(), tableName, maxRows);

		// 获取列信息
		List<String> columns = schemaTableData.getColumns();
		int orderIndex = columns.indexOf(query.getOrder());

		List<List<Object>> rows = schemaTableData.getRows();
		// 对数据进行排序（如果有排序需求）
		if (orderIndex != -1) {
			final int index = orderIndex;
			rows.sort((row1, row2) -> {
				Comparable value1 = (Comparable) row1.get(index);
				Comparable value2 = (Comparable) row2.get(index);
				return query.isAsc() ? value1.compareTo(value2) : value2.compareTo(value1);
			});
		}

		// 计算分页起始和结束索引
		int startIndex = (query.getPage() - 1) * query.getLimit();
		int endIndex = Math.min(startIndex + query.getLimit(), rows.size());

		// 获取分页数据
		List<List<Object>> paginatedRows = rows.subList(startIndex, endIndex);

		// 将每行数据转换为 Map<String, Object> 并放入 List
		List<Map<String, Object>> dataList = paginatedRows.stream().map(row -> {
			return SqlUtils.convertRow(columns, row);
		}).collect(Collectors.toList());

		// 构建 PageResult 对象
		PageResult<Map<String, Object>> result = new PageResult<>(
				dataList,
				rows.size()
		);

		return result;
	}

	public Map<String, String> TableheaderGet(Long datatableid){
		DataProjectCacheBean project = getProject();
		String tableName=baseMapper.selectById(datatableid).getTableName();
		IMetaDataByJdbcService service = new MetaDataByJdbcServiceImpl(ProductTypeEnum.getByIndex(project.getDbType()));

		// 设置合理的最大行数限制
		int maxRows = 50000000;
		// 查询所有表数据
		SchemaTableData schemaTableData = service.queryTableData(project.getDbUrl(), project.getDbUsername(), project.getDbPassword(), project.getDbSchema(), tableName, maxRows);

		// 获取列信息
		List<String> column = schemaTableData.getColumns();
		Map<String, String> columns=SqlUtils.convertColumns(schemaTableData.getColumns());
		return columns;
	}

	private List<ColumnDescriptionVo> convertToColumnDescriptionVo(List<String> columns, List<List<Object>> rows, long datatableId, String tableName) {
		List<ColumnDescriptionVo> columnDescriptionVos = new ArrayList<>();
		for (List<Object> row : rows) {
			ColumnDescriptionVo vo = new ColumnDescriptionVo();
			for (int i = 0; i < columns.size(); i++) {
				String columnName = columns.get(i);
				Object value = row.get(i);
				switch (columnName) {
					case "fieldName":
						vo.setFieldName((String) value);
						break;
					case "remarks":
						vo.setRemarks((String) value);
						break;
					case "labelName":
						vo.setLabelName((String) value);
						break;
					case "fieldTypeName":
						vo.setFieldTypeName((String) value);
						break;
					case "displaySize":
						vo.setDisplaySize((Integer) value);
						break;
					case "scaleSize":
						vo.setScaleSize((Integer) value);
						break;
					case "defaultValue":
						vo.setDefaultValue((String) value);
						break;
					case "isNullable":
						vo.setNullable((Boolean) value);
						break;
					case "isPk":
						vo.setPk((Boolean) value);
						break;
					case "isAutoIncrement":
						vo.setAutoIncrement((Boolean) value);
						break;
					default:
						// Handle other fields if necessary
						break;
				}
			}
			vo.setDatatableId(datatableId);
			vo.setDatatableName(tableName);
			columnDescriptionVos.add(vo);
		}
		return columnDescriptionVos;
	}


	public void modifyDataTable(DataTableVO query) {
		// 获取数据接入信息
		DataAccessDto dataAccess = dataAccessApi.getById(query.getDataAccessId()).getData();
		if (dataAccess==null) {
			throw new RuntimeException("Data access not found");
		}
		// 获取源端数据库信息
		Long datasourceid=dataDatabaseApi.getDatasourceIdbyDatabaseId(dataAccess.getSourceDatabaseId()).getData();
		DataSourceDto dataSource = dataDatabaseApi.getById(datasourceid).getData();
		if (dataSource==null) {
			throw new RuntimeException("Data Source not found");
		}
		// 修改源端数据库中的表名称和注释
		modifySourceDatabaseTable(dataSource, query);

		// 同步修改到数据中台库
		/*DataTableEntity dataTable = dataTableRepository.findById(request.getDatatableId())
				.orElseThrow(() -> new RuntimeException("Data table not found"));
		dataTable.setDatatableName(request.getDatatableName());
		dataTable.setRemarks(request.getRemarks());
		dataTable.setRecentlySyncTime(request.getRecentlySyncTime());
		dataTableRepository.save(dataTable);*/
	}

	public void newDataTable(DataTableVO query){
		// 获取数据接入信息
		Long dataaccessid=dataAccessApi.getAccessIDbydatabaseID(query.getDatabaseId());
		DataAccessDto dataAccess = dataAccessApi.getById(dataaccessid).getData();
		if (dataAccess==null) {
			throw new RuntimeException("Data access not found");
		}
		// 获取源端数据库信息
		Long datasourceid=dataDatabaseApi.getDatasourceIdbyDatabaseId(dataAccess.getSourceDatabaseId()).getData();
		DataSourceDto dataSource = dataDatabaseApi.getById(datasourceid).getData();
		if (dataSource==null) {
			throw new RuntimeException("Data Source not found");
		}

		createSourceDatabaseTable(dataSource,query);

	}

	private void modifySourceDatabaseTable(DataSourceDto dataSource, DataTableVO query) {
		DataTableEntity entity=baseMapper.selectById(query.getDatatableId());
		String databaseName=query.getDatabaseName();
		// 创建数据库连接
		try (Connection connection = DriverManager.getConnection(dataSource.getJdbcUrl(), dataSource.getUserName(), dataSource.getPassword())) {
			// 修改表名称
			String renameTableSql = String.format("ALTER TABLE `%s`.`%s` RENAME TO `%s`.`%s`", databaseName, entity.getTableName().replace("ods_",""), databaseName, query.getDatatableName());
			try (Statement stmt = connection.createStatement()) {
				stmt.execute(renameTableSql);
			}

			// 修改表注释
			String commentTableSql = String.format("ALTER TABLE `%s`.`%s` COMMENT = '%s'", databaseName, query.getDatatableName(), query.getRemarks());
			try (Statement stmt = connection.createStatement()) {
				stmt.execute(commentTableSql);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed to modify source database table", e);
		}
	}

	public void createSourceDatabaseTable(DataSourceDto dataSource, DataTableVO query) {
		String databaseName = query.getDatabaseName();
		String datatableName = query.getDatatableName();
		String remarks = query.getRemarks();

		// 构建 CREATE TABLE SQL 语句
		String createTableSql = String.format("CREATE TABLE `%s`.`%s` (id BIGINT PRIMARY KEY) COMMENT='%s'",
				databaseName, datatableName, remarks);

		// 创建数据库连接并执行 SQL 语句
		try (Connection connection = DriverManager.getConnection(dataSource.getJdbcUrl(), dataSource.getUserName(), dataSource.getPassword())) {
			try (Statement stmt = connection.createStatement()) {
				stmt.execute(createTableSql);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed to create source database table", e);
		}
	}

	public void deleteODSDatabaseTable(Long id){
		String tableName=baseMapper.selectById(id).getTableName();
		DataProjectCacheBean project = getProject();
		String databaseName=project.getDbName();
		DataSourceDto dataSource = new DataSourceDto();
		dataSource.setDatabaseName(project.getDbName());
		dataSource.setJdbcUrl(project.getDbUrl());
		dataSource.setUserName(project.getDbUsername());
		dataSource.setPassword(project.getDbPassword());
		// 创建数据库连接
		String jdbcUrl = dataSource.getJdbcUrl();
		if (!jdbcUrl.contains(databaseName)) {
			if (jdbcUrl.contains("?")) {
				jdbcUrl = jdbcUrl.replace("?", "/" + databaseName + "?");
			} else {
				jdbcUrl += "/" + databaseName;
			}
		}
		// 打印 JDBC URL 和连接参数
		log.debug("JDBC URL: " + jdbcUrl);
		log.debug("Database User: " + dataSource.getUserName());
		log.debug("Database Name: " + databaseName);
		//log.debug("Table Name to be deleted: " + tableName);
		try (Connection connection = DriverManager.getConnection(jdbcUrl, dataSource.getUserName(), dataSource.getPassword())) {
			// 检查表是否存在
			String checkTableSql = String.format("SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = '%s' AND table_name = '%s'", databaseName, tableName);
			log.debug("Executing SQL: " + checkTableSql);
			try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(checkTableSql)) {
				if (rs.next() && rs.getInt(1) == 0) {
					throw new SQLException("Table " + tableName + " does not exist in database " + databaseName);
				}
			}

			// 删除表
			String dropTableSql = String.format("DROP TABLE `%s`", tableName);
			log.debug("Executing SQL: " + dropTableSql);
			try (Statement stmt = connection.createStatement()) {
				stmt.execute(dropTableSql);
			}

			log.debug("Table " + tableName + " has been successfully deleted.");
		} catch (SQLException e) {
			log.error("Failed to delete table " + tableName, e);
			throw new RuntimeException("Failed to delete table " + tableName, e);
		}
		List<Long> ids=new ArrayList<>();
		ids.add(id);
		removeByIds(ids);

	}
	public void deleteSourceDatabaseTable(Long id) {
		String tableName=baseMapper.selectById(id).getTableName();
		tableName=tableName.replace("ods_","");
		// 获取源端数据库信息
		Long accessid=baseMapper.selectById(id).getDataAccessId();
		log.debug(accessid.toString());//获取accessid
		Long sourcedatabaseid=dataAccessApi.getById(accessid).getData().getSourceDatabaseId();
		log.debug(sourcedatabaseid.toString());//获取databaseid
		Long datasourceid=dataDatabaseApi.getDatasourceIdbyDatabaseId(sourcedatabaseid).getData();
		log.debug(datasourceid.toString());//获取sourcedatabaseid
		DataSourceDto dataSource = dataDatabaseApi.getById(datasourceid).getData();
		dataSource.setDatabaseName(dataDatabaseApi.getDataBaseBamebyId(sourcedatabaseid).getData());
		if (dataSource==null) {
			throw new RuntimeException("Data Source not found");
		}
		String databaseName = dataSource.getDatabaseName();
		// 构建 DROP TABLE SQL 语句
		String dropTableSql = String.format("DROP TABLE `%s`.`%s`", databaseName, tableName);

		// 创建数据库连接并执行 SQL 语句
		try (Connection connection = DriverManager.getConnection(dataSource.getJdbcUrl(), dataSource.getUserName(), dataSource.getPassword())) {
			try (Statement stmt = connection.createStatement()) {
				stmt.execute(dropTableSql);
				System.out.println("Table " + tableName + " has been successfully deleted.");
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed to delete source database table", e);
		}
	}

	public boolean updateTableData(UpdateDataQuery query){
		// 获取数据接入信息
		DataAccessDto dataAccess = dataAccessApi.getById(getaccessidbydatabaseid(query.getDatatableId())).getData();
		if (dataAccess == null) {
			throw new RuntimeException("Data access not found");
		}

		// 获取源端数据库信息
		Long datasourceid = dataDatabaseApi.getDatasourceIdbyDatabaseId(dataAccess.getSourceDatabaseId()).getData();
		DataSourceDto dataSource = dataDatabaseApi.getById(datasourceid).getData();
		if (dataSource == null) {
			throw new RuntimeException("Data Source not found");
		}

		// 修改源端数据库中的表数据
		boolean ifsuccess=modifySourceDatabaseTableData(dataSource, query);
		if(ifsuccess) {
			Long accessid=baseMapper.selectById(query.getDatatableId()).getDataAccessId();
			quartzDataAccessApi.handRun(accessid);
		}
		return ifsuccess;

	}

	private boolean modifySourceDatabaseTableData(DataSourceDto dataSource, UpdateDataQuery query) {
		// 构建更新 SQL 语句
		List<String> primaryKeyColumnS = getPrimaryKeyColumn(query.getDatatableId());
		String tableName = baseMapper.selectById(query.getDatatableId()).getTableName().replace("ods_", "");
		Long databaseId = dataAccessApi.getById(getaccessidbydatabaseid(query.getDatatableId())).getData().getSourceDatabaseId();
		String databasename = dataDatabaseApi.getDataBaseBamebyId(databaseId).getData();

		try (Connection connection = DriverManager.getConnection(dataSource.getJdbcUrl(), dataSource.getUserName(), dataSource.getPassword());
			 Statement useDbStmt = connection.createStatement()) { // 选择数据库
			useDbStmt.execute("USE " + databasename);

			for (Map<String, Object> data : query.getRows()) { // 为每个数据项构建更新 SQL 语句
				StringBuilder sql = new StringBuilder("UPDATE `").append(databasename).append("`.`").append(tableName).append("` SET ");
				List<Object> params = new ArrayList<>();
				List<Object> primaryKeyValues = new ArrayList<>();

				data.forEach((column, value) -> {
					if (!primaryKeyColumnS.contains(column)) {
						sql.append("`").append(column).append("` = ?, ");
						params.add(value);
					} else {
						primaryKeyValues.add(value);
					}
				});

				// 删除最后一个逗号和空格
				sql.setLength(sql.length() - 2);
				sql.append(" WHERE ");
				for (int i = 0; i < primaryKeyColumnS.size(); i++) {
					if (i > 0) {
						sql.append(" AND ");
					}
					sql.append("`").append(primaryKeyColumnS.get(i)).append("` = ?");
				}
				params.addAll(primaryKeyValues);

				// 打印 SQL 语句和参数
				System.out.println("Executing SQL: " + sql);
				System.out.println("With parameters: " + params);

				// 执行更新
				try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
					for (int i = 0; i < params.size(); i++) {
						stmt.setObject(i + 1, params.get(i));
					}
					stmt.executeUpdate();
				}
			}
			return true;
		} catch (SQLException e) {
			// 打印异常信息
			e.printStackTrace();
			throw new RuntimeException("Failed to update table data in source database", e);
		}

	}

	private boolean insertMultipleToSourceDatabaseTableData(DataSourceDto dataSource, UpdateDataQuery query) {

		if (query.getRows().isEmpty()) {
			return false;
		}

		String tableName=baseMapper.selectById(query.getDatatableId()).getTableName().replace("ods_","");
		Long databaseId=dataAccessApi.getById(getaccessidbydatabaseid(query.getDatatableId())).getData().getSourceDatabaseId();
		String databasename=dataDatabaseApi.getDataBaseBamebyId(databaseId).getData();

		// 获取第一条数据的列名
		Map<String, Object> firstData = query.getRows().get(0);
		StringBuilder sql = new StringBuilder("INSERT INTO ")
				.append("`").append(databasename).append("`.`").append(tableName).append("` (");

		// 构建列名部分
		for (String column : firstData.keySet()) {
			sql.append("`").append(column).append("`, ");
		}
		// 去掉末尾的逗号和空格
		sql.setLength(sql.length() - 2);
		sql.append(") VALUES (");

		// 构建占位符部分
		for (@SuppressWarnings("unused") String column : firstData.keySet()) {
			sql.append("?, ");
		}
		// 去掉末尾的逗号和空格
		sql.setLength(sql.length() - 2);
		sql.append(")");

		// 打印 SQL 语句
		System.out.println("Executing SQL: " + sql);

		try (Connection connection = DriverManager.getConnection(dataSource.getJdbcUrl(), dataSource.getUserName(), dataSource.getPassword());
			 PreparedStatement stmt = connection.prepareStatement(sql.toString())) {

			for (Map<String, Object> data : query.getRows()) {
				int paramIndex = 1;
				for (String column : firstData.keySet()) {
					stmt.setObject(paramIndex++, data.get(column));
				}
				stmt.addBatch();
			}

			int[] rowsAffected = stmt.executeBatch();
			return rowsAffected.length == query.getRows().size();
		} catch (SQLException e) {
			// 打印异常信息
			e.printStackTrace();
			throw new RuntimeException("Failed to insert multiple rows into table in source database", e);
		}
	}

	public boolean deleteMultipleFromSourceDatabaseTableData(DataSourceDto dataSource, Long tableId, List<List<Object>> primaryKeyValues, List<String> primaryKeyColumns) {
		String tableName = baseMapper.selectById(tableId).getTableName().replace("ods_", "");
		Long databaseId = dataAccessApi.getById(getaccessidbydatabaseid(tableId)).getData().getSourceDatabaseId();
		String databasename = dataDatabaseApi.getDataBaseBamebyId(databaseId).getData();

		// 如果主键值列表为空，则不需要删除
		if (primaryKeyValues.isEmpty() || primaryKeyColumns.isEmpty()) {
			return false;
		}

		// 构建 WHERE 子句
		StringBuilder whereClause = new StringBuilder();
		for (int i = 0; i < primaryKeyColumns.size(); i++) {
			if (i > 0) {
				whereClause.append(" AND ");
			}
			whereClause.append("`").append(primaryKeyColumns.get(i)).append("` = ?");
		}

		StringBuilder sql = new StringBuilder("DELETE FROM `")
				.append(databasename).append("`.`").append(tableName).append("` WHERE ")
				.append(whereClause);

		// 打印 SQL 语句
		System.out.println("Executing SQL: " + sql);

		try (Connection connection = DriverManager.getConnection(dataSource.getJdbcUrl(), dataSource.getUserName(), dataSource.getPassword());
			 PreparedStatement stmt = connection.prepareStatement(sql.toString())) {

			for (List<Object> primaryKeyValueList : primaryKeyValues) {
				if (primaryKeyValueList.size() != primaryKeyColumns.size()) {
					throw new IllegalArgumentException("Primary key value list size must match primary key column list size");
				}
				for (int i = 0; i < primaryKeyValueList.size(); i++) {
					stmt.setObject(i + 1, primaryKeyValueList.get(i));
				}
				stmt.addBatch();
			}

			int[] rowsAffected = stmt.executeBatch();
			return rowsAffected.length == primaryKeyValues.size();
		} catch (SQLException e) {
			// 打印异常信息
			e.printStackTrace();
			throw new RuntimeException("Failed to delete multiple rows from table in source database", e);
		}
	}

	public List<String> getPrimaryKeyColumn(Long datatableid) {
		// 获取项目信息
		DataProjectCacheBean project = getProject();
		// 获取表名
		String tableName = baseMapper.selectById(datatableid).getTableName();
		// 创建服务实例
		IMetaDataByJdbcService service = new MetaDataByJdbcServiceImpl(ProductTypeEnum.getByIndex(project.getDbType()));

		List<String> pks =  service.queryTablePrimaryKeys(project.getDbUrl(), project.getDbUsername(), project.getDbPassword(), project.getDbSchema(), tableName);

		// 若未找到主键列，返回null
		return pks;
	}

	public Long getaccessidbydatabaseid(Long id){
		if (id == null) {
			throw new NullPointerException("DataAccess object is null for databaseId: " + id);
		}
		return baseMapper.selectById(id).getDataAccessId();
	}

	public PageResult<ColumnDescriptionVo> Columnpage(DataFieldQuery query) {
		DataTableEntity tableEntity = getById(query.getDatatableId());
		if (tableEntity == null) {
			return new PageResult<>(new ArrayList<>(), 0);
		}

		DataProjectCacheBean project = getProject();
		IMetaDataByJdbcService metaDataService = new MetaDataByJdbcServiceImpl(ProductTypeEnum.getByIndex(project.getDbType()));

		List<ColumnDescription> columnDescriptions = metaDataService.queryTableColumnMeta(
				project.getDbUrl(), project.getDbUsername(), project.getDbPassword(),
				project.getDbSchema(), tableEntity.getTableName()
		);

		List<ColumnDescriptionVo> columnDescriptionVos = columnDescriptions.stream().map(columnDescription -> {
			ColumnDescriptionVo columnDescriptionVo = new ColumnDescriptionVo();
			columnDescriptionVo.setFieldName(columnDescription.getFieldName());
			columnDescriptionVo.setRemarks(columnDescription.getRemarks());
			columnDescriptionVo.setFieldTypeName(columnDescription.getFieldTypeName());
			columnDescriptionVo.setDisplaySize(columnDescription.getDisplaySize());
			columnDescriptionVo.setScaleSize(columnDescription.getScaleSize());
			columnDescriptionVo.setDefaultValue(columnDescription.getDefaultValue());
			columnDescriptionVo.setNullable(columnDescription.isNullable());
			columnDescriptionVo.setPk(columnDescription.isPk());
			columnDescriptionVo.setAutoIncrement(columnDescription.isAutoIncrement());
			columnDescriptionVo.setDatatableId(query.getDatatableId());
			columnDescriptionVo.setDatatableName(tableEntity.getTableName());
			return columnDescriptionVo;
		}).collect(Collectors.toList());

		// Pagination
		int startIndex = (query.getPage() - 1) * query.getLimit();
		int endIndex = Math.min(query.getPage() * query.getLimit(), columnDescriptionVos.size());
		List<ColumnDescriptionVo> pageList = columnDescriptionVos.subList(startIndex, endIndex);

		return new PageResult<>(pageList, columnDescriptionVos.size());
	}
}