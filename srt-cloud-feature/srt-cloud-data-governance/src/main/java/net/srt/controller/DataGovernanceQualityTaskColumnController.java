package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceQualityTaskColumnConvert;
import net.srt.entity.DataGovernanceQualityTaskColumnEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataGovernanceQualityTaskColumnQuery;
import net.srt.service.DataGovernanceQualityTaskColumnService;
import net.srt.vo.DataGovernanceQualityTaskColumnVO;
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
 * 数据治理-字段检测记录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-06-23
 */
@RestController
@RequestMapping("/quality-task-column")
@Tag(name = "数据治理-字段检测记录")
@AllArgsConstructor
public class DataGovernanceQualityTaskColumnController {
	private final DataGovernanceQualityTaskColumnService dataGovernanceQualityTaskColumnService;

	@GetMapping("page")
	@Operation(summary = "分页")
	public Result<PageResult<DataGovernanceQualityTaskColumnVO>> page(@Valid DataGovernanceQualityTaskColumnQuery query) {
		PageResult<DataGovernanceQualityTaskColumnVO> page = dataGovernanceQualityTaskColumnService.page(query);

		return Result.ok(page);
	}

	@GetMapping("{id}")
	@Operation(summary = "信息")
	public Result<DataGovernanceQualityTaskColumnVO> get(@PathVariable("id") Long id) {
		DataGovernanceQualityTaskColumnEntity entity = dataGovernanceQualityTaskColumnService.getById(id);

		return Result.ok(DataGovernanceQualityTaskColumnConvert.INSTANCE.convert(entity));
	}


	@DeleteMapping
	@Operation(summary = "删除")
	public Result<String> delete(@RequestBody List<Long> idList) {
		dataGovernanceQualityTaskColumnService.delete(idList);

		return Result.ok();
	}
}
