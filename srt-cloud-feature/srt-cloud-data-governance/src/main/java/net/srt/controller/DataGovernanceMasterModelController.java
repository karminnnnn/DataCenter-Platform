package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceMasterModelConvert;
import net.srt.entity.DataGovernanceMasterModelEntity;
import net.srt.framework.common.utils.Result;
import net.srt.service.DataGovernanceMasterModelService;
import net.srt.vo.DataGovernanceMasterModelVO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 数据治理-主数据模型
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-09-27
 */
@RestController
@RequestMapping("/master-model")
@Tag(name = "数据治理-主数据模型")
@AllArgsConstructor
public class DataGovernanceMasterModelController {
	private final DataGovernanceMasterModelService dataGovernanceMasterModelService;

	@GetMapping("/by-id/{id}")
	@Operation(summary = "根据id获取")
	public Result<DataGovernanceMasterModelVO> getById(@PathVariable("id") Long id) {
		DataGovernanceMasterModelEntity entity = dataGovernanceMasterModelService.getById(id);

		return Result.ok(DataGovernanceMasterModelConvert.INSTANCE.convert(entity));
	}

	@GetMapping("{catalogId}")
	@Operation(summary = "信息")
	public Result<DataGovernanceMasterModelVO> get(@PathVariable("catalogId") Long catalogId) {
		DataGovernanceMasterModelEntity entity = dataGovernanceMasterModelService.getByCatalogId(catalogId);

		return Result.ok(DataGovernanceMasterModelConvert.INSTANCE.convert(entity));
	}

	@PostMapping
	@Operation(summary = "保存")
	public Result<DataGovernanceMasterModelVO> save(@RequestBody DataGovernanceMasterModelVO vo) {
		return Result.ok(DataGovernanceMasterModelConvert.INSTANCE.convert(dataGovernanceMasterModelService.save(vo)));
	}

	@PutMapping
	@Operation(summary = "修改")
	public Result<DataGovernanceMasterModelVO> update(@RequestBody @Valid DataGovernanceMasterModelVO vo) {
		return Result.ok(DataGovernanceMasterModelConvert.INSTANCE.convert(dataGovernanceMasterModelService.update(vo)));
	}

	@DeleteMapping("{id}")
	@Operation(summary = "删除")
	public Result<String> delete(@PathVariable Long id) {
		dataGovernanceMasterModelService.delete(id);

		return Result.ok();
	}

	@PutMapping("/release/{id}")
	@Operation(summary = "发布")
	public Result<DataGovernanceMasterModelVO> release(@PathVariable Long id) {
		return Result.ok(DataGovernanceMasterModelConvert.INSTANCE.convert(dataGovernanceMasterModelService.release(id)));
	}

	@PutMapping("/cancel-release/{id}")
	@Operation(summary = "取消发布")
	public Result<DataGovernanceMasterModelVO> cancelRelease(@PathVariable Long id) {
		return Result.ok(DataGovernanceMasterModelConvert.INSTANCE.convert(dataGovernanceMasterModelService.cancelRelease(id)));
	}
}
