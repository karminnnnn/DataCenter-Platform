package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.api.DataAccessApiImpl;
import net.srt.api.DataDatabaseApiImpl;
import net.srt.api.module.data.integrate.dto.DataAccessDto;
import net.srt.api.module.data.integrate.dto.DataSourceDto;
import net.srt.api.module.data.integrate.dto.DataTableDto;
import net.srt.api.module.quartz.QuartzDataAccessApi;
import net.srt.constants.DataHouseLayer;
import net.srt.convert.DataOdsConvert;
import net.srt.dao.DataTableDao;
import net.srt.entity.DataAccessEntity;
import net.srt.entity.DataSourceEntity;
import net.srt.entity.DataTableEntity;
import net.srt.framework.common.cache.bean.DataProjectCacheBean;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.BeanUtil;
import net.srt.framework.common.utils.SqlUtils;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataTableQuery;
import net.srt.query.TableDataQuery;
import net.srt.service.DataTableService;
import net.srt.vo.ColumnDescriptionVo;
import net.srt.vo.DataTableVO;
import net.srt.vo.SchemaTableDataVo;
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
import java.util.ArrayList;
import java.util.List;
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
		//DataTableEntity entity = DataOdsConvert.INSTANCE.convert(vo);
		String Newtablename=vo.getDatatableName();
		DataTableEntity entity=baseMapper.selectById(vo.getDatatableId());
		String OldtableName=entity.getTableName();
		modifyDataTable(vo);
		quartzDataAccessApi.handRun(vo.getDataAccessId());
		if(OldtableName!=Newtablename)
			{deleteODSDatabaseTable(vo.getDatatableId());}
	//	updateById(entity);
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

	@Override
	public SchemaTableDataVo getTableData(String tableName) {
		DataProjectCacheBean project = getProject();
		IMetaDataByJdbcService service = new MetaDataByJdbcServiceImpl(ProductTypeEnum.getByIndex(project.getDbType()));
		SchemaTableData schemaTableData = service.queryTableData(project.getDbUrl(), project.getDbUsername(), project.getDbPassword(), project.getDbSchema(), tableName, 50);
		return SchemaTableDataVo.builder().columns(SqlUtils.convertColumns(schemaTableData.getColumns())).rows(SqlUtils.convertRows(schemaTableData.getColumns(), schemaTableData.getRows())).build();
	}

	@Override
	public void saveTableData(TableDataQuery request) {
		// 假设数据是保存到某个数据表中，可以根据业务逻辑实现具体保存逻辑
		//Long datatableId = request.getDatatableId();
		//List<Map<String, Object>> rows = request.getRows();

		// 实现数据保存逻辑，根据具体业务需求
		//for (Map<String, Object> row : rows) {
			// 保存每行数据的具体逻辑
			// 例如，将 row 中的数据保存到数据库中
		}

	public PageResult<SchemaTableDataVo> pageTableData(TableDataQuery query){
		return new PageResult<SchemaTableDataVo>(new ArrayList<>(), 0);
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
}