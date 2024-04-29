package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceMetamodelPropertyConvert;
import net.srt.entity.DataGovernanceMetamodelPropertyEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataGovernanceMetamodelPropertyQuery;
import net.srt.service.DataGovernanceMetamodelPropertyService;
import net.srt.vo.DataGovernanceMetamodelPropertyVO;
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
 * 数据治理-元模型属性
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-03-28
 */
@RestController
@RequestMapping("metamodel-property")
@Tag(name = "数据治理-元模型属性")
@AllArgsConstructor
public class DataGovernanceMetamodelPropertyController {
	private final DataGovernanceMetamodelPropertyService dataGovernanceMetamodelPropertyService;


	@GetMapping("/properties/{metaModelId}")
	@Operation(summary = "根据id获取属性列表")
	public Result<List<DataGovernanceMetamodelPropertyVO>> properties(@PathVariable Long metaModelId) {
		List<DataGovernanceMetamodelPropertyVO> properties = dataGovernanceMetamodelPropertyService.properties(metaModelId);

		return Result.ok(properties);
	}

	@GetMapping("page")
	@Operation(summary = "分页")
	public Result<PageResult<DataGovernanceMetamodelPropertyVO>> page(@Valid DataGovernanceMetamodelPropertyQuery query) {
		PageResult<DataGovernanceMetamodelPropertyVO> page = dataGovernanceMetamodelPropertyService.page(query);
		return Result.ok(page);
	}

	@GetMapping("{id}")
	@Operation(summary = "信息")
	public Result<DataGovernanceMetamodelPropertyVO> get(@PathVariable("id") Long id) {
		DataGovernanceMetamodelPropertyEntity entity = dataGovernanceMetamodelPropertyService.getById(id);

		return Result.ok(DataGovernanceMetamodelPropertyConvert.INSTANCE.convert(entity));
	}

	@PostMapping
	@Operation(summary = "保存")
	public Result<String> save(@RequestBody DataGovernanceMetamodelPropertyVO vo) {
		dataGovernanceMetamodelPropertyService.save(vo);

		return Result.ok();
	}

	@PutMapping
	@Operation(summary = "修改")
	public Result<String> update(@RequestBody @Valid DataGovernanceMetamodelPropertyVO vo) {
		dataGovernanceMetamodelPropertyService.update(vo);

		return Result.ok();
	}

	@DeleteMapping
	@Operation(summary = "删除")
	public Result<String> delete(@RequestBody List<Long> idList) {
		dataGovernanceMetamodelPropertyService.delete(idList);

		return Result.ok();
	}
}
