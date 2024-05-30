package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataOdsConvert;
import net.srt.entity.DataTableEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataTableQuery;
import net.srt.query.TableDataQuery;
import net.srt.service.DataTableService;
import net.srt.vo.ColumnDescriptionVo;
import net.srt.vo.DataTableVO;
import net.srt.vo.SchemaTableDataVo;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

		return Result.ok(DataOdsConvert.INSTANCE.convert(entity));
	}

	@DeleteMapping
	@Operation(summary = "数据表删除")
	public Result<String> delete(@RequestBody List<Long> idList) {
		dataTableService.delete(idList);
		return Result.ok();
	}

	@PostMapping("tabledata")
	@Operation(summary = "数据表数据保存")
	public Result<String> saveTableData(@RequestBody TableDataQuery request) {
		dataTableService.saveTableData(request);
		return Result.ok("数据保存成功");
	}
}
