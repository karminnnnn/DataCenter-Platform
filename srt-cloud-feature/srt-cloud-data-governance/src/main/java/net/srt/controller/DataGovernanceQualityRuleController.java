package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceQualityRuleConvert;
import net.srt.entity.DataGovernanceQualityRuleEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataGovernanceQualityRuleQuery;
import net.srt.service.DataGovernanceQualityRuleService;
import net.srt.vo.DataGovernanceQualityRuleVO;
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
 * 数据治理-质量规则
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-28
 */
@RestController
@RequestMapping("/quality-rule")
@Tag(name = "数据治理-质量规则")
@AllArgsConstructor
public class DataGovernanceQualityRuleController {
	private final DataGovernanceQualityRuleService dataGovernanceQualityRuleService;


	@GetMapping("list")
	@Operation(summary = "查询列表")
	public Result<List<DataGovernanceQualityRuleVO>> list() {
		List<DataGovernanceQualityRuleEntity> list = dataGovernanceQualityRuleService.list();

		return Result.ok(DataGovernanceQualityRuleConvert.INSTANCE.convertList(list));
	}

	@GetMapping("page")
	@Operation(summary = "分页")
	public Result<PageResult<DataGovernanceQualityRuleVO>> page(@Valid DataGovernanceQualityRuleQuery query) {
		PageResult<DataGovernanceQualityRuleVO> page = dataGovernanceQualityRuleService.page(query);

		return Result.ok(page);
	}

	@GetMapping("{id}")
	@Operation(summary = "信息")
	public Result<DataGovernanceQualityRuleVO> get(@PathVariable("id") Long id) {
		DataGovernanceQualityRuleEntity entity = dataGovernanceQualityRuleService.getById(id);

		return Result.ok(DataGovernanceQualityRuleConvert.INSTANCE.convert(entity));
	}

	@PostMapping
	@Operation(summary = "保存")
	public Result<String> save(@RequestBody DataGovernanceQualityRuleVO vo) {
		dataGovernanceQualityRuleService.save(vo);

		return Result.ok();
	}

	@PutMapping
	@Operation(summary = "修改")
	public Result<String> update(@RequestBody @Valid DataGovernanceQualityRuleVO vo) {
		dataGovernanceQualityRuleService.update(vo);

		return Result.ok();
	}

	@DeleteMapping
	@Operation(summary = "删除")
	public Result<String> delete(@RequestBody List<Long> idList) {
		dataGovernanceQualityRuleService.delete(idList);

		return Result.ok();
	}
}
