package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.api.DataAccessApiImpl;
import net.srt.api.DataDatabaseApiImpl;
import net.srt.api.module.data.integrate.dto.DataAccessDto;
import net.srt.convert.DataOdsConvert;
import net.srt.entity.DataTableEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.*;
import net.srt.service.DataTableService;
import net.srt.vo.ColumnDescriptionVo;
import net.srt.vo.DataTableVO;
import net.srt.vo.SchemaDataVo;
import net.srt.vo.SchemaTableDataVo;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static dm.jdbc.util.DriverUtil.log;

/**
 * 数据集成-贴源数据
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-11-07
 */
@RestController
@RequestMapping("ods")
@Tag(name = "数据集成-数据表管理")
@AllArgsConstructor
public class DataTableController {
	private final DataTableService dataTableService;
	private DataAccessApiImpl dataAccessApi;
	private DataDatabaseApiImpl dataDatabaseApi;

	@GetMapping("page")
	@Operation(summary = "数据表分页")
	public Result<PageResult<DataTableVO>> page(@Valid DataTableQuery query) {
		PageResult<DataTableVO> page = dataTableService.page(query);
		return Result.ok(page);
	}

	@GetMapping("{id}")
	@Operation(summary = "获取特定数据表信息")
	public Result<DataTableVO> get(@PathVariable("id") Long id) {
		DataTableEntity entity = dataTableService.getById(id);
		DataTableVO dataTableVO=DataOdsConvert.INSTANCE.convert(entity);
		String datatablename=dataTableVO.getDatatableName();
		datatablename=datatablename.replace("ods_","");
		dataTableVO.setDatatableName(datatablename);
		DataAccessDto dataAccessDto=dataAccessApi.getById(entity.getDataAccessId()).getData();
		Long databaseid=dataAccessDto.getSourceDatabaseId();
		dataTableVO.setDatabaseId(databaseid);
		dataTableVO.setDatabaseName(dataDatabaseApi.getDataBaseBamebyId(databaseid).getData());
		return Result.ok(dataTableVO);
	}

	@DeleteMapping
	@Operation(summary = "数据表删除")
	public Result<String> delete(@RequestBody List<Long> idList) {
		dataTableService.delete(idList);
		return Result.ok();
	}


	@PostMapping
	@Operation(summary = "数据表保存")
	public Result<String> save(@RequestBody @Valid DataTableVO vo) {
		dataTableService.save(vo);
		return Result.ok("Data Table saved successfully");
	}

	@PutMapping
	@Operation(summary = "数据表修改")
	public Result<String> update(@RequestBody @Valid DataTableVO vo) {
		dataTableService.update(vo);
		return Result.ok("Column info updated successfully");
	}

	@PostMapping("tabledata")
	@Operation(summary = "数据表数据保存")
	public Result<String> saveTableData(@RequestBody UpdateDataQuery request) {
		boolean result =dataTableService.saveTableData(request);
		if (result) {
			return Result.ok("Save successful");
		} else {
			return Result.error("Save failed");
		}
	}

	@GetMapping("tabledata/page")
	@Operation(summary = "数据表数据分页")
	public Result<PageResult<Map<String, Object>>> pageTableData(@Valid TableDataQuery query) {
		PageResult<Map<String, Object>> page = dataTableService.pageTableData(query);
        return Result.ok(page);
    }

	@PutMapping("tabledata")
	@Operation(summary = "数据表数据修改")
    public Result<String> updateTableData(@RequestBody UpdateDataQuery query) {
		boolean result = dataTableService.updateTableData(query);
		if (result) {
			return Result.ok("Update successful");
		} else {
			return Result.error("Update failed");
		}
    }

	@DeleteMapping("tabledata")
	@Operation(summary = "数据表数据删除")
	public Result<String> deleteTableData (@RequestBody DeleteDataQuery query){
		boolean result = dataTableService.deleteTableData(query.getIdList(),query.getPrimaryKeyColumn(),query.getDatatableId());
		if (result) {
			return Result.ok("Delete successful");
		} else {
			return Result.error("Delete failed");
		}
	}

	@GetMapping("tabledata/headers/{datatableId}")
	@Operation(summary = "数据表表头获取")
	public  Result<List<String>> getTableHeader(@PathVariable("datatableId") Long datatableId){
		return Result.ok(dataTableService.TableheaderGet(datatableId));
	}
}
