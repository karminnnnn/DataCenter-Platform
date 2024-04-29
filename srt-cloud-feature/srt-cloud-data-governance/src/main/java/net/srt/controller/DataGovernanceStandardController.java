package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceStandardConvert;
import net.srt.dto.StandardCheckDto;
import net.srt.entity.DataGovernanceStandardEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataGovernanceStandardQuery;
import net.srt.service.DataGovernanceStandardService;
import net.srt.vo.DataGovernanceStandardVO;
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
 * 数据治理-数据标准
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-05
 */
@RestController
@RequestMapping("/data-standard")
@Tag(name = "数据治理-数据标准")
@AllArgsConstructor
public class DataGovernanceStandardController {
	private final DataGovernanceStandardService dataGovernanceStandardService;

	@GetMapping("page")
	@Operation(summary = "分页")
	public Result<PageResult<DataGovernanceStandardVO>> page(@Valid DataGovernanceStandardQuery query) {
		PageResult<DataGovernanceStandardVO> page = dataGovernanceStandardService.page(query);

		return Result.ok(page);
	}

	@GetMapping("{id}")
	@Operation(summary = "信息")
	public Result<DataGovernanceStandardVO> get(@PathVariable("id") Long id) {
		DataGovernanceStandardEntity entity = dataGovernanceStandardService.getById(id);

		return Result.ok(DataGovernanceStandardConvert.INSTANCE.convert(entity));
	}

	@PostMapping
	@Operation(summary = "保存")
	public Result<String> save(@RequestBody DataGovernanceStandardVO vo) {
		dataGovernanceStandardService.save(vo);

		return Result.ok();
	}

	@PutMapping
	@Operation(summary = "修改")
	public Result<String> update(@RequestBody @Valid DataGovernanceStandardVO vo) {
		dataGovernanceStandardService.update(vo);

		return Result.ok();
	}

	@DeleteMapping
	@Operation(summary = "删除")
	public Result<String> delete(@RequestBody List<Long> idList) {
		dataGovernanceStandardService.delete(idList);

		return Result.ok();
	}

	@Deprecated
	@GetMapping("/{id}/online")
	@Operation(summary = "上线")
	public Result<String> online(@PathVariable("id") Long id) {
		dataGovernanceStandardService.online(id);
		return Result.ok();
	}

	@Deprecated
	@PostMapping("/{id}/offline")
	@Operation(summary = "下线")
	public Result<String> offline(@PathVariable("id") Long id) {
		dataGovernanceStandardService.offline(id);
		return Result.ok();
	}

	@GetMapping("/table-code/list")
	@Operation(summary = "获取标准码表列表")
	public Result<List<DataGovernanceStandardVO>> listTableCode() {
		return Result.ok(dataGovernanceStandardService.listTableCode());
	}


	@GetMapping("/check/{metadataId}/{standardId}")
	@Operation(summary = "标准检测")
	public Result<StandardCheckDto> checkStandard(@PathVariable Long metadataId, @PathVariable Long standardId) {
		return Result.ok(dataGovernanceStandardService.checkStandard(metadataId, standardId));
	}
}
