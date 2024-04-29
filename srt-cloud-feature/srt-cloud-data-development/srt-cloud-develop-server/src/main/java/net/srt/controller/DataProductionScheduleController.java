package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.dto.Flow;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.model.ConsoleLog;
import net.srt.query.DataProductionScheduleQuery;
import net.srt.query.DataProductionScheduleRecordQuery;
import net.srt.service.DataProductionScheduleService;
import net.srt.vo.DataProductionScheduleNodeRecordVO;
import net.srt.vo.DataProductionScheduleRecordVO;
import net.srt.vo.DataProductionScheduleVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 数据生产-作业调度
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-01-12
 */
@RestController
@RequestMapping("schedule")
@Tag(name = "数据生产-作业调度")
@AllArgsConstructor
public class DataProductionScheduleController {
	private final DataProductionScheduleService dataProductionScheduleService;

	@GetMapping("page")
	@Operation(summary = "分页")
	@PreAuthorize("hasAuthority('data-development:schedule:page')")
	public Result<PageResult<DataProductionScheduleVO>> page(@Valid DataProductionScheduleQuery query) {
		PageResult<DataProductionScheduleVO> page = dataProductionScheduleService.page(query);
		return Result.ok(page);
	}

	@GetMapping("{id}")
	@Operation(summary = "信息")
	@PreAuthorize("hasAuthority('data-development:schedule:info')")
	public Result<Flow> get(@PathVariable("id") Long id) {
		return Result.ok(dataProductionScheduleService.get(id));
	}

	@PostMapping
	@Operation(summary = "保存")
	@PreAuthorize("hasAuthority('data-development:schedule:save')")
	public Result<String> save(@RequestBody Flow flow) {
		dataProductionScheduleService.save(flow);
		return Result.ok();
	}

	@PostMapping("/run/{id}")
	@Operation(summary = "执行（返回log的id）")
	@PreAuthorize("hasAuthority('data-development:schedule:run')")
	public Result<String> run(@PathVariable Integer id) {
		return Result.ok(dataProductionScheduleService.run(id));
	}

	@PostMapping("/release/{id}")
	@Operation(summary = "发布")
	@PreAuthorize("hasAuthority('data-development:schedule:release')")
	public Result<String> release(@PathVariable Integer id) {
		dataProductionScheduleService.release(id);
		return Result.ok();
	}

	@PostMapping("/cancle/{id}")
	@Operation(summary = "取消发布")
	@PreAuthorize("hasAuthority('data-development:schedule:cancle')")
	public Result<String> cancle(@PathVariable Integer id) {
		dataProductionScheduleService.cancle(id);
		return Result.ok();
	}

	@GetMapping("/log/{recordId}")
	@Operation(summary = "获取流程图执行的日志")
	public Result<ConsoleLog> getLog(@PathVariable Integer recordId) {
		return Result.ok(dataProductionScheduleService.getLog(recordId));
	}

	@GetMapping("/node-info/{recordId}")
	@Operation(summary = "根据调度记录id获取节点调度记录")
	public Result<List<DataProductionScheduleNodeRecordVO>> listNodeRecord(@PathVariable Integer recordId) {
		return Result.ok(dataProductionScheduleService.listNodeRecord(recordId));
	}

	@GetMapping("/record/page")
	@Operation(summary = "调度日志记录分页")
	@PreAuthorize("hasAuthority('data-development:schedule:record:page')")
	public Result<PageResult<DataProductionScheduleRecordVO>> pageRecord(@Valid DataProductionScheduleRecordQuery query) {
		PageResult<DataProductionScheduleRecordVO> page = dataProductionScheduleService.pageRecord(query);
		return Result.ok(page);
	}

	@DeleteMapping("/record")
	@Operation(summary = "删除调度日志记录")
	@PreAuthorize("hasAuthority('data-development:schedule:record:delete')")
	public Result<String> delRecord(@RequestBody List<Long> idList) {
		dataProductionScheduleService.delRecord(idList);
		return Result.ok();
	}

	@GetMapping("/node-record/{nodeRecordId}")
	@Operation(summary = "根据节点调度id获取节点调度记录")
	public Result<DataProductionScheduleNodeRecordVO> getNodeRecord(@PathVariable Integer nodeRecordId) {
		return Result.ok(dataProductionScheduleService.getNodeRecord(nodeRecordId));
	}

	@DeleteMapping
	@Operation(summary = "删除")
	@PreAuthorize("hasAuthority('data-development:schedule:delete')")
	public Result<String> delete(@RequestBody List<Long> idList) {
		dataProductionScheduleService.delete(idList);

		return Result.ok();
	}
}
