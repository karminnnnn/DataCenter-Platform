package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceMasterDistributeConvert;
import net.srt.entity.DataGovernanceMasterDistributeEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataGovernanceMasterDistributeQuery;
import net.srt.service.DataGovernanceMasterDistributeService;
import net.srt.vo.DataGovernanceMasterDistributeVO;
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
 * 数据治理-主数据派发
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-10-08
 */
@RestController
@RequestMapping("/master-distribute")
@Tag(name = "数据治理-主数据派发")
@AllArgsConstructor
public class DataGovernanceMasterDistributeController {
	private final DataGovernanceMasterDistributeService dataGovernanceMasterDistributeService;

	@GetMapping("page")
	@Operation(summary = "分页")
	public Result<PageResult<DataGovernanceMasterDistributeVO>> page(@Valid DataGovernanceMasterDistributeQuery query) {
		PageResult<DataGovernanceMasterDistributeVO> page = dataGovernanceMasterDistributeService.page(query);
		return Result.ok(page);
	}

	@GetMapping("{id}")
	@Operation(summary = "信息")
	public Result<DataGovernanceMasterDistributeVO> get(@PathVariable("id") Long id) {
		DataGovernanceMasterDistributeEntity entity = dataGovernanceMasterDistributeService.getById(id);
		return Result.ok(DataGovernanceMasterDistributeConvert.INSTANCE.convert(entity));
	}

	@PostMapping
	@Operation(summary = "保存")
	public Result<String> save(@RequestBody DataGovernanceMasterDistributeVO vo) {
		dataGovernanceMasterDistributeService.save(vo);
		return Result.ok();
	}

	@PutMapping
	@Operation(summary = "修改")
	public Result<String> update(@RequestBody @Valid DataGovernanceMasterDistributeVO vo) {
		dataGovernanceMasterDistributeService.update(vo);
		return Result.ok();
	}

	@DeleteMapping
	@Operation(summary = "删除")
	public Result<String> delete(@RequestBody List<Long> idList) {
		dataGovernanceMasterDistributeService.delete(idList);
		return Result.ok();
	}

	@PutMapping("/release/{id}")
	@Operation(summary = "发布")
	public Result<String> release(@PathVariable Long id) {
		dataGovernanceMasterDistributeService.release(id);
		return Result.ok();
	}

	@PutMapping("/cancel/{id}")
	@Operation(summary = "取消发布")
	public Result<String> cancel(@PathVariable Long id) {
		dataGovernanceMasterDistributeService.cancel(id);
		return Result.ok();
	}

	@PutMapping("/hand-run/{id}")
	@Operation(summary = "手动执行")
	public Result<String> handRun(@PathVariable Long id) {
		dataGovernanceMasterDistributeService.handRun(id);
		return Result.ok();
	}
}
