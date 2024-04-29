package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceMasterColumnConvert;
import net.srt.entity.DataGovernanceMasterColumnEntity;
import net.srt.framework.common.utils.Result;
import net.srt.service.DataGovernanceMasterColumnService;
import net.srt.vo.DataGovernanceMasterColumnVO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 数据治理-主数据模型字段
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-09-27
 */
@RestController
@RequestMapping("/master-column")
@Tag(name = "数据治理-主数据模型字段")
@AllArgsConstructor
public class DataGovernanceMasterColumnController {
	private final DataGovernanceMasterColumnService dataGovernanceMasterColumnService;

	@GetMapping("{id}")
	@Operation(summary = "信息")
	public Result<DataGovernanceMasterColumnVO> get(@PathVariable("id") Long id) {
		DataGovernanceMasterColumnEntity entity = dataGovernanceMasterColumnService.getById(id);

		return Result.ok(DataGovernanceMasterColumnConvert.INSTANCE.convert(entity));
	}

	@PostMapping
	@Operation(summary = "保存")
	public Result<String> save(@RequestBody DataGovernanceMasterColumnVO vo) {
		dataGovernanceMasterColumnService.save(vo);

		return Result.ok();
	}

	@PutMapping
	@Operation(summary = "修改")
	public Result<String> update(@RequestBody @Valid DataGovernanceMasterColumnVO vo) {
		dataGovernanceMasterColumnService.update(vo);

		return Result.ok();
	}

	@DeleteMapping
	@Operation(summary = "删除")
	public Result<String> delete(@RequestBody List<Long> idList) {
		dataGovernanceMasterColumnService.delete(idList);

		return Result.ok();
	}

	@GetMapping("/middle-db/{tableName}/columns")
	@Operation(summary = "获取中台库字段信息")
	public Result<List<DataGovernanceMasterColumnVO>> middleDbClumnInfo(@PathVariable String tableName) {
		return Result.ok(dataGovernanceMasterColumnService.middleDbClumnInfo(tableName));
	}
}
