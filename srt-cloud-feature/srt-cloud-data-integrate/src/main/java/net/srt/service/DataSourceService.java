package net.srt.service;

import net.srt.dto.SqlConsole;
import net.srt.dto.TableInfo;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.*;
import net.srt.vo.DataSourceVO;
import net.srt.query.DataSourceQuery;
import net.srt.entity.DataSourceEntity;

import java.util.List;
import java.util.Map;

/**
 * 数据集成-数据库管理
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-10-09
 */
public interface DataSourceService extends BaseService<DataSourceEntity> {

	PageResult<DataSourceVO> page(DataSourceQuery query);

	List<String> save(DataSourceVO vo);

	void update(DataSourceVO vo);

	void delete(List<Long> idList);

	void testOnline(DataSourceVO vo);

	List<TableVo> getTablesById(Long id,String databasename);

	SchemaTableDataVo getTableDataBySql(Integer id, SqlConsole sqlConsole);

	List<DataSourceVO> listAll();

	List<TreeNodeVo> listTree(Long id);

	List<ColumnDescriptionVo> getColumnInfo(Long id, String tableName,String databasename);

	List<ColumnDescriptionVo> getColumnInfoBySql(Long id, SqlConsole sqlConsole);

	SqlGenerationVo getSqlGeneration(Long id, String tableName, String tableRemarks);

	List<TreeNodeVo> listMiddleDbTree();

	List<ColumnDescriptionVo> middleDbClumnInfo(String tableName);

	SqlGenerationVo getMiddleDbSqlGeneration(String tableName, String tableRemarks);

	TableInfo getTableInfo(String tableName);

	TableInfo saveTableInfo(TableInfo tableInfo);

	void deleteTableInfo(String tableName);


	List<Map<String, Object>> getDatabaseInfoByDataSourceIds(List<Long> dataSourceIds);

	public Integer getDatasourceIdByDatabaseId(Long databaseId);

	public String getDatabasenameByID(Long databaseId);
}
