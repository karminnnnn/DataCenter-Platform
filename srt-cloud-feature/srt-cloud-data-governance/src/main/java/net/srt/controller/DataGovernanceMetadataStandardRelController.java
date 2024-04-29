package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceMetadataStandardRelConvert;
import net.srt.entity.DataGovernanceMetadataStandardRelEntity;
import net.srt.framework.common.utils.Result;
import net.srt.service.DataGovernanceMetadataStandardRelService;
import net.srt.vo.DataGovernanceMetadataStandardRelVO;
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
 * 数据治理-元数据标准关联表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-23
 */
@RestController
@RequestMapping("standard-rel")
@Tag(name = "数据治理-元数据标准关联表")
@AllArgsConstructor
public class DataGovernanceMetadataStandardRelController {
	private final DataGovernanceMetadataStandardRelService dataGovernanceMetadataStandardRelService;

	@GetMapping("/{metadataId}/metadata-rel")
	@Operation(summary = "根据metadataId获取标准字段")
	public Result<DataGovernanceMetadataStandardRelVO> getMetadataRel(@PathVariable("metadataId") Long metadataId) {
		DataGovernanceMetadataStandardRelVO standardRelVO = dataGovernanceMetadataStandardRelService.getMetadataRel(metadataId);

		return Result.ok(standardRelVO);
	}

	@GetMapping("{id}")
	@Operation(summary = "信息")
	public Result<DataGovernanceMetadataStandardRelVO> get(@PathVariable("id") Long id) {
		DataGovernanceMetadataStandardRelEntity entity = dataGovernanceMetadataStandardRelService.getById(id);

		return Result.ok(DataGovernanceMetadataStandardRelConvert.INSTANCE.convert(entity));
	}

	@PostMapping
	@Operation(summary = "保存")
	public Result<String> save(@RequestBody DataGovernanceMetadataStandardRelVO vo) {
		dataGovernanceMetadataStandardRelService.save(vo);

		return Result.ok();
	}

	@DeleteMapping("/{metadataId}/{standardId}")
	@Operation(summary = "删除")
	public Result<String> delete(@PathVariable Long metadataId, @PathVariable Long standardId) {
		dataGovernanceMetadataStandardRelService.delete(metadataId, standardId);
		return Result.ok();
	}
}
