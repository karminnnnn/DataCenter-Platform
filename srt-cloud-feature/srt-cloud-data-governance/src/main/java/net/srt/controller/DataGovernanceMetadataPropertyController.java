package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceMetadataPropertyConvert;
import net.srt.entity.DataGovernanceMetadataPropertyEntity;
import net.srt.framework.common.utils.Result;
import net.srt.service.DataGovernanceMetadataPropertyService;
import net.srt.vo.DataGovernanceMetadataPropertyVO;
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
 * 数据治理-元数据属性值
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-03-29
 */
@RestController
@RequestMapping("metadata-property")
@Tag(name = "数据治理-元数据属性值")
@AllArgsConstructor
public class DataGovernanceMetadataPropertyController {
	private final DataGovernanceMetadataPropertyService dataGovernanceMetadataPropertyService;

	@GetMapping("{id}")
	@Operation(summary = "信息")
	public Result<DataGovernanceMetadataPropertyVO> get(@PathVariable("id") Long id) {
		DataGovernanceMetadataPropertyEntity entity = dataGovernanceMetadataPropertyService.getById(id);

		return Result.ok(DataGovernanceMetadataPropertyConvert.INSTANCE.convert(entity));
	}

	@PostMapping
	@Operation(summary = "保存")
	public Result<String> save(@RequestBody DataGovernanceMetadataPropertyVO vo) {
		dataGovernanceMetadataPropertyService.save(vo);

		return Result.ok();
	}

	@PutMapping
	@Operation(summary = "修改")
	public Result<String> update(@RequestBody @Valid DataGovernanceMetadataPropertyVO vo) {
		dataGovernanceMetadataPropertyService.update(vo);

		return Result.ok();
	}

	@DeleteMapping
	@Operation(summary = "删除")
	public Result<String> delete(@RequestBody List<Long> idList) {
		dataGovernanceMetadataPropertyService.delete(idList);

		return Result.ok();
	}
}
