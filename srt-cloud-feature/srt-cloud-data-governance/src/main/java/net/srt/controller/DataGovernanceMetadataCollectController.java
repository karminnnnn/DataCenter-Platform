package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceMetadataCollectConvert;
import net.srt.entity.DataGovernanceMetadataCollectEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataGovernanceMetadataCollectQuery;
import net.srt.service.DataGovernanceMetadataCollectService;
import net.srt.vo.DataGovernanceMetadataCollectVO;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * 数据治理-元数据采集
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-04-01
 */
@RestController
@RequestMapping("/metadata-collect")
@Tag(name = "数据治理-元数据采集")
@AllArgsConstructor
public class DataGovernanceMetadataCollectController {
	private final DataGovernanceMetadataCollectService dataGovernanceMetadataCollectService;

	@GetMapping("page")
	@Operation(summary = "分页")
	@PreAuthorize("hasAuthority('data-governance:metadata-collect:page')")
	public Result<PageResult<DataGovernanceMetadataCollectVO>> page(@Valid DataGovernanceMetadataCollectQuery query) {
		PageResult<DataGovernanceMetadataCollectVO> page = dataGovernanceMetadataCollectService.page(query);

		return Result.ok(page);
	}

	@GetMapping("{id}")
	@Operation(summary = "信息")
	@PreAuthorize("hasAuthority('data-governance:metadata-collect:info')")
	public Result<DataGovernanceMetadataCollectVO> get(@PathVariable("id") Long id) {
		DataGovernanceMetadataCollectEntity entity = dataGovernanceMetadataCollectService.getById(id);

		return Result.ok(DataGovernanceMetadataCollectConvert.INSTANCE.convert(entity));
	}

	@PostMapping
	@Operation(summary = "保存")
	@PreAuthorize("hasAuthority('data-governance:metadata-collect:save')")
	public Result<String> save(@RequestBody DataGovernanceMetadataCollectVO vo) {
		dataGovernanceMetadataCollectService.save(vo);

		return Result.ok();
	}

	@PutMapping
	@Operation(summary = "修改")
	@PreAuthorize("hasAuthority('data-governance:metadata-collect:update')")
	public Result<String> update(@RequestBody @Valid DataGovernanceMetadataCollectVO vo) {
		dataGovernanceMetadataCollectService.update(vo);
		return Result.ok();
	}

	@PutMapping("/release/{id}")
	@Operation(summary = "发布")
	@PreAuthorize("hasAuthority('data-governance:metadata-collect:release')")
	public Result<String> release(@PathVariable Long id) {
		dataGovernanceMetadataCollectService.release(id);
		return Result.ok();
	}

	@PutMapping("/cancel/{id}")
	@Operation(summary = "取消发布")
	@PreAuthorize("hasAuthority('data-governance:metadata-collect:cancel')")
	public Result<String> cancel(@PathVariable Long id) {
		dataGovernanceMetadataCollectService.cancel(id);
		return Result.ok();
	}

	@PostMapping("/hand-run/{id}")
	@Operation(summary = "手动执行")
	@PreAuthorize("hasAuthority('data-governance:metadata-collect:hand-run')")
	public Result<String> handRun(@PathVariable Long id) {
		dataGovernanceMetadataCollectService.handRun(id);
		return Result.ok();
	}

	@DeleteMapping
	@Operation(summary = "删除")
	@PreAuthorize("hasAuthority('data-governance:metadata-collect:delete')")
	public Result<String> delete(@RequestBody List<Long> idList) {
		dataGovernanceMetadataCollectService.delete(idList);

		return Result.ok();
	}
}
