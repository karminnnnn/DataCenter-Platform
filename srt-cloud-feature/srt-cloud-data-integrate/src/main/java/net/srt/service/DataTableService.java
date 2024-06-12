package net.srt.service;

import net.srt.entity.DataTableEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.query.DataTableQuery;
import net.srt.query.TableDataQuery;
import net.srt.query.UpdateDataQuery;
import net.srt.vo.DataTableVO;
import net.srt.vo.SchemaDataVo;

import java.util.List;
import java.util.Map;

/**
 * 数据集成-贴源数据
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-11-07
 */
public interface DataTableService extends BaseService<DataTableEntity> {

	PageResult<DataTableVO> page(DataTableQuery query);

	void save(DataTableVO vo);

	void update(DataTableVO vo);

	void delete(List<Long> idList);

	DataTableEntity getByTableName(Long projectId, String tableName);

	//List<ColumnDescriptionVo> getColumnInfo(String tableName);

	//SchemaTableDataVo getTableData(String tableName);

	boolean saveTableData(UpdateDataQuery request);

	boolean updateTableData(UpdateDataQuery query);

	boolean deleteTableData(List<Object> idList,Long datatableId );


	Long getaccessidbydatabaseid(Long id);

	PageResult<Map<String, Object>> pageTableData(TableDataQuery query);

	Map<String, String> TableheaderGet(Long datatableid);
}
