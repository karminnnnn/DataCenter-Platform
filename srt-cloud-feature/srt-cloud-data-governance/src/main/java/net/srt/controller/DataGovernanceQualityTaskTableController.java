package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceQualityTaskTableConvert;
import net.srt.entity.DataGovernanceQualityTaskTableEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataGovernanceQualityTaskTableQuery;
import net.srt.service.DataGovernanceQualityTaskTableService;
import net.srt.vo.DataGovernanceQualityTaskTableVO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 数据治理-表检测记录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-06-23
 */
@RestController
@RequestMapping("/quality-task-table")
@Tag(name = "数据治理-表检测记录")
@AllArgsConstructor
public class DataGovernanceQualityTaskTableController {
	private final DataGovernanceQualityTaskTableService dataGovernanceQualityTaskTableService;

	@GetMapping("page")
	@Operation(summary = "分页")
	public Result<PageResult<DataGovernanceQualityTaskTableVO>> page(@Valid DataGovernanceQualityTaskTableQuery query) {
		PageResult<DataGovernanceQualityTaskTableVO> page = dataGovernanceQualityTaskTableService.page(query);

		return Result.ok(page);
	}

	@GetMapping("{id}")
	@Operation(summary = "信息")
	public Result<DataGovernanceQualityTaskTableVO> get(@PathVariable("id") Long id) {
		DataGovernanceQualityTaskTableEntity entity = dataGovernanceQualityTaskTableService.getById(id);

		return Result.ok(DataGovernanceQualityTaskTableConvert.INSTANCE.convert(entity));
	}

	@DeleteMapping
	@Operation(summary = "删除")
	public Result<String> delete(@RequestBody List<Long> idList) {
		dataGovernanceQualityTaskTableService.delete(idList);

		return Result.ok();
	}
}
