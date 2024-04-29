package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.dto.StudioExecuteDto;
import net.srt.flink.common.result.SqlExplainResult;
import net.srt.flink.core.job.JobResult;
import net.srt.flink.core.result.SelectResult;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.model.ConsoleLog;
import net.srt.query.DataProductionTaskHistoryQuery;
import net.srt.query.DataProductionTaskSavepointsQuery;
import net.srt.service.DataProductionTaskService;
import net.srt.vo.DataProductionTaskHistoryVO;
import net.srt.vo.DataProductionTaskInstanceHistoryVO;
import net.srt.vo.DataProductionTaskSavepointsVO;
import net.srt.vo.DataProductionTaskVO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 数据生产任务
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-05
 */
@RestController
@RequestMapping("task")
@Tag(name = "数据生产任务")
@AllArgsConstructor
public class DataProductionTaskController {
	private final DataProductionTaskService dataProductionTaskService;

	@GetMapping("{id}")
	@Operation(summary = "信息")
	public Result<DataProductionTaskVO> get(@PathVariable("id") Long id) {
		DataProductionTaskVO vo = dataProductionTaskService.get(id);
		return Result.ok(vo);
	}

	@PostMapping
	@Operation(summary = "保存")
	public Result<DataProductionTaskVO> save(@RequestBody DataProductionTaskVO vo) {
		return Result.ok(dataProductionTaskService.save(vo));
	}

	@PutMapping
	@Operation(summary = "修改")
	public Result<DataProductionTaskVO> update(@RequestBody @Valid DataProductionTaskVO vo) {
		return Result.ok(dataProductionTaskService.update(vo));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除")
	public Result<String> delete(@PathVariable Long id) {
		dataProductionTaskService.delete(id);
		return Result.ok();
	}

	/**
	 * 解释Sql
	 */
	@PostMapping("/explain-sql")
	@Operation(summary = "检查sql有效性")
	public Result<List<SqlExplainResult>> explainSql(@RequestBody StudioExecuteDto studioExecuteDto) {
		return Result.ok(dataProductionTaskService.explainSql(studioExecuteDto));
	}

	/**
	 * 执行Sql
	 */
	@PostMapping("/just-execute-sql")
	@Operation(summary = "只执行sql")
	public Result<JobResult> justExecuteSql(@RequestBody StudioExecuteDto studioExecuteDto) {
		return Result.ok(dataProductionTaskService.justExecuteSql(studioExecuteDto));
	}

	/**
	 * 执行Sql
	 */
	@PostMapping("/execute-sql")
	@Operation(summary = "执行sql")
	public Result<JobResult> executeSql(@RequestBody StudioExecuteDto studioExecuteDto) {
		return Result.ok(dataProductionTaskService.executeSql(studioExecuteDto));
	}

	/**
	 * 提交作业
	 */
	@Operation(summary = "提交作业")
	@PostMapping(value = "/submit/{taskId}")
	public Result<JobResult> submit(@PathVariable Long taskId) {
		return Result.ok(dataProductionTaskService.submitTask(taskId));
	}

	@Operation(summary = "清空当前用户日志")
	@GetMapping("/clear-log")
	public Result<String> clearLog() {
		dataProductionTaskService.clearLog();
		return Result.ok();
	}

	@Operation(summary = "清空当前用户日志，不清除key")
	@GetMapping("/clear-log-without-key")
	public Result<String> clearLogWithOutKey() {
		dataProductionTaskService.clearLogWithOutKey();
		return Result.ok();
	}

	@Operation(summary = "获取当前用户执行日志")
	@GetMapping("/console-log")
	public Result<ConsoleLog> getConsoleLog() {
		return Result.ok(dataProductionTaskService.getConsoleLog());
	}

	@Operation(summary = "停止日志")
	@GetMapping("/end-log")
	public Result<String> endConsoleLog() {
		dataProductionTaskService.endConsoleLog();
		return Result.ok();
	}

	@Operation(summary = "根据jobId获取flinkSql的执行结果")
	@GetMapping("/job-data")
	public Result<SelectResult> getJobDataByJobId(@RequestParam String jobId) {
		return Result.ok(dataProductionTaskService.getJobDataByJobId(jobId));
	}

	@GetMapping("/history/page")
	@Operation(summary = "分页获取任务执行历史")
	public Result<PageResult<DataProductionTaskHistoryVO>> pageHistory(@Valid DataProductionTaskHistoryQuery query) {
		PageResult<DataProductionTaskHistoryVO> page = dataProductionTaskService.pageHistory(query);
		return Result.ok(page);
	}

	@GetMapping("/history/instance-error")
	@Operation(summary = "获取flink实例错误信息")
	public Result<String> getInstanceError(@RequestParam Integer historyId) {
		return Result.ok(dataProductionTaskService.getInstanceError(historyId));
	}

	@GetMapping("/history/cluster-info")
	@Operation(summary = "获取historyId获取集群信息")
	public Result<DataProductionTaskInstanceHistoryVO> getHistoryClusterInfo(@RequestParam Integer historyId) {
		return Result.ok(dataProductionTaskService.getHistoryClusterInfo(historyId));
	}

	@DeleteMapping("/history")
	@Operation(summary = "删除任务执行历史")
	public Result<String> delHistory(@RequestBody List<Long> idList) {
		dataProductionTaskService.delHistory(idList);
		return Result.ok();
	}

	/**
	 * savePoint
	 */
	@Operation(summary = "对任务进行savepoint操作")
	@GetMapping(value = "/savepoint")
	public Result savepoint(@RequestParam Integer historyId, @RequestParam String type) {
		dataProductionTaskService.savepoint(historyId, type);
		return Result.ok();
	}

	/**
	 * savePoint分页
	 */
	@Operation(summary = "根据任务id获取savepoint列表")
	@GetMapping(value = "/savepoint/page")
	public Result<PageResult<DataProductionTaskSavepointsVO>> pageSavepoint(@Valid DataProductionTaskSavepointsQuery query) {
		return Result.ok(dataProductionTaskService.pageSavepoint(query));
	}

	/**
	 * savePoint删除
	 */
	@Operation(summary = "手动删除savepoint")
	@DeleteMapping(value = "/savepoint")
	public Result delSavepoint(@RequestBody List<Long> idList) {
		dataProductionTaskService.delSavepoint(idList);
		return Result.ok();
	}

	/**
	 * 获取flink公共代码块列表
	 */
	@Operation(summary = "获取flink公共代码块列表")
	@GetMapping(value = "/env-list")
	public Result<List<DataProductionTaskVO>> listEnv() {
		return Result.ok(dataProductionTaskService.listEnv());
	}
}
