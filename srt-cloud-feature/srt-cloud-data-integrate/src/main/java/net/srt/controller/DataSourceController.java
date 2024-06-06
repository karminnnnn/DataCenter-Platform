package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataSourceConvert;
import net.srt.dto.SqlConsole;
import net.srt.dto.TableInfo;
import net.srt.entity.DataSourceEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.query.DataSourceQuery;
import net.srt.service.DataDatabaseService;
import net.srt.service.DataSourceService;
import net.srt.vo.ColumnDescriptionVo;
import net.srt.vo.DataSourceVO;
import net.srt.vo.SchemaTableDataVo;
import net.srt.vo.SqlGenerationVo;
import net.srt.vo.TableVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据集成-数据库管理
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-10-09
 */
@RestController
@RequestMapping("datasource")
@Tag(name = "数据集成-数据源管理")
@AllArgsConstructor
public class DataSourceController {
	private final DataSourceService DataSourceService;

	@GetMapping("page")
	@Operation(summary = "分页")
	//@PreAuthorize("hasAuthority('data-integrate:database:page')")
	public Result<PageResult<DataSourceVO>> page(@ModelAttribute @Valid DataSourceQuery query) {
		PageResult<DataSourceVO> page = DataSourceService.page(query);

		return Result.ok(page);
	}

	@GetMapping("{id}")
	@Operation(summary = "信息")
	@PreAuthorize("hasAuthority('data-integrate:database:info')")
	public Result<DataSourceVO> get(@PathVariable("id") Long id) {
		DataSourceEntity entity = DataSourceService.getById(id);

		return Result.ok(DataSourceConvert.INSTANCE.convert(entity));
	}

	@PostMapping
	@Operation(summary = "保存")
	@PreAuthorize("hasAuthority('data-integrate:database:save')")
	public Result<String> save(@RequestBody DataSourceVO vo) {
		DataSourceService.save(vo);

		return Result.ok();
	}

	@PutMapping
	@Operation(summary = "修改")
	@PreAuthorize("hasAuthority('data-integrate:database:update')")
	public Result<String> update(@RequestBody @Valid DataSourceVO vo) {
		DataSourceService.update(vo);

		return Result.ok();
	}

	@DeleteMapping
	@Operation(summary = "删除")
	@PreAuthorize("hasAuthority('data-integrate:database:delete')")
	public Result<String> delete(@RequestBody List<Long> idList) {
		DataSourceService.delete(idList);
		return Result.ok();
	}

	@PostMapping("/test-online")
	@Operation(summary = "测试连接")
	public Result<String> testOnline(@RequestBody @Valid DataSourceVO vo) {
		DataSourceService.testOnline(vo);
		return Result.ok();
	}

	@GetMapping("/tables/{id}")
	@Operation(summary = "根据数据库id获取表相关信息")
	public Result<List<TableVo>> getTablesById(@PathVariable Long id) {
		Long ID=Long.valueOf(DataSourceService.getDatasourceIdByDatabaseId(id));
		List<TableVo> tableVos = DataSourceService.getTablesById(ID);
		return Result.ok(tableVos);
	}

	@PostMapping("/table-data/{id}")
	@Operation(summary = "根据sql获取数据")
	public Result<SchemaTableDataVo> getTableDataBySql(@PathVariable Integer id, @RequestBody SqlConsole sqlConsole) {
		SchemaTableDataVo schemaTableDataVo = DataSourceService.getTableDataBySql(id, sqlConsole);
		return Result.ok(schemaTableDataVo);
	}

	@GetMapping("/list-all")
	@Operation(summary = "获取当前用户所能看到的的数据库")
	public Result<List<Map<String, Object>>> listAll() {
		List<DataSourceVO> list = DataSourceService.listAll();
		List<Long> dataSourceIds = list.stream()
				.map(DataSourceVO::getId)
				.collect(Collectors.toList());
		List<Map<String, Object>> databaseInfoList = DataSourceService.getDatabaseInfoByDataSourceIds(dataSourceIds);
		return Result.ok(databaseInfoList);
	}

	@GetMapping("/list-tree/{id}")
	@Operation(summary = "获取库目录树")
	public Result<List<TreeNodeVo>> listTree(@PathVariable Long id) {
		List<TreeNodeVo> list = DataSourceService.listTree(id);
		return Result.ok(list);
	}

	@GetMapping("/middle-db/list-tree")
	@Operation(summary = "获取中台库（当前项目）目录树")
	public Result<List<TreeNodeVo>> listMiddleDbTree() {
		List<TreeNodeVo> list = DataSourceService.listMiddleDbTree();
		return Result.ok(list);
	}

	@PostMapping("/{id}/sql/columns")
	@Operation(summary = "获取sql获取字段信息")
	public Result<List<ColumnDescriptionVo>> columnInfoBySql(@PathVariable Long id, @RequestBody SqlConsole sqlConsole) {
		return Result.ok(DataSourceService.getColumnInfoBySql(id, sqlConsole));
	}

	@GetMapping("/{id}/{tableName}/columns")
	@Operation(summary = "获取字段信息")
	public Result<List<ColumnDescriptionVo>> columnInfo(@PathVariable Long id, @PathVariable String tableName) {
		return Result.ok(DataSourceService.getColumnInfo(id, tableName));
	}

	@GetMapping("/middle-db/{tableName}/columns")
	@Operation(summary = "获取中台库字段信息")
	public Result<List<ColumnDescriptionVo>> middleDbClumnInfo(@PathVariable String tableName) {
		return Result.ok(DataSourceService.middleDbClumnInfo(tableName));
	}

	@GetMapping("/{id}/{tableName}/sql-generation")
	@Operation(summary = "获取sql信息")
	public Result<SqlGenerationVo> getSqlGeneration(@PathVariable Long id, @PathVariable String tableName, String tableRemarks) {
		return Result.ok(DataSourceService.getSqlGeneration(id, tableName, tableRemarks));
	}

	@GetMapping("/middle-db/{tableName}/sql-generation")
	@Operation(summary = "获取中台库sql信息")
	public Result<SqlGenerationVo> getMiddleDbSqlGeneration(@PathVariable String tableName, String tableRemarks) {
		return Result.ok(DataSourceService.getMiddleDbSqlGeneration(tableName, tableRemarks));
	}

	@GetMapping("/middle-db/table-info/{tableName}")
	@Operation(summary = "获取表信息")
	public Result<TableInfo> getTableInfo(@PathVariable String tableName) {
		return Result.ok(DataSourceService.getTableInfo(tableName));
	}

	@PostMapping("/middle-db/table-info")
	@Operation(summary = "保存表（建表）")
	public Result<TableInfo> saveTableInfo(@RequestBody TableInfo tableInfo) {
		return Result.ok(DataSourceService.saveTableInfo(tableInfo));
	}

	@DeleteMapping("/middle-db/table-info/{tableName}")
	@Operation(summary = "删除表")
	public Result<String> deleteTableInfo(@PathVariable String tableName) {
		DataSourceService.deleteTableInfo(tableName);
		return Result.ok();
	}

}
