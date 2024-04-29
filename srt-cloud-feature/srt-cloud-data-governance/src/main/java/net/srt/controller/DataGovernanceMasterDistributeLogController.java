package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceMasterDistributeLogConvert;
import net.srt.entity.DataGovernanceMasterDistributeLogEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataGovernanceMasterDistributeLogQuery;
import net.srt.service.DataGovernanceMasterDistributeLogService;
import net.srt.vo.DataGovernanceMasterDistributeLogVO;
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
 * 数据治理-主数据派发日志
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-10-08
 */
@RestController
@RequestMapping("/master-distribute-log")
@Tag(name = "数据治理-主数据派发日志")
@AllArgsConstructor
public class DataGovernanceMasterDistributeLogController {
	private final DataGovernanceMasterDistributeLogService dataGovernanceMasterDistributeLogService;

	@GetMapping("page")
	@Operation(summary = "分页")
	public Result<PageResult<DataGovernanceMasterDistributeLogVO>> page(@Valid DataGovernanceMasterDistributeLogQuery query) {
		PageResult<DataGovernanceMasterDistributeLogVO> page = dataGovernanceMasterDistributeLogService.page(query);

		return Result.ok(page);
	}

	@GetMapping("{id}")
	@Operation(summary = "信息")
	public Result<DataGovernanceMasterDistributeLogVO> get(@PathVariable("id") Long id) {
		DataGovernanceMasterDistributeLogEntity entity = dataGovernanceMasterDistributeLogService.getById(id);

		return Result.ok(DataGovernanceMasterDistributeLogConvert.INSTANCE.convert(entity));
	}

	@PostMapping
	@Operation(summary = "保存")
	public Result<String> save(@RequestBody DataGovernanceMasterDistributeLogVO vo) {
		dataGovernanceMasterDistributeLogService.save(vo);

		return Result.ok();
	}

	@PutMapping
	@Operation(summary = "修改")
	public Result<String> update(@RequestBody @Valid DataGovernanceMasterDistributeLogVO vo) {
		dataGovernanceMasterDistributeLogService.update(vo);

		return Result.ok();
	}

	@DeleteMapping
	@Operation(summary = "删除")
	public Result<String> delete(@RequestBody List<Long> idList) {
		dataGovernanceMasterDistributeLogService.delete(idList);

		return Result.ok();
	}
}
