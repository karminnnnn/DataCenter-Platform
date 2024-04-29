package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataProductionClusterConvert;
import net.srt.entity.DataProductionClusterEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataProductionClusterQuery;
import net.srt.service.DataProductionClusterService;
import net.srt.vo.DataProductionClusterVO;
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
 * 数据生产-集群实例
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-01
 */
@RestController
@RequestMapping("/cluster")
@Tag(name = "数据生产-集群实例")
@AllArgsConstructor
public class DataProductionClusterController {
	private final DataProductionClusterService dataProductionClusterService;


	@GetMapping("page")
	@Operation(summary = "分页")
	@PreAuthorize("hasAuthority('data-development:cluster:page')")
	public Result<PageResult<DataProductionClusterVO>> page(@Valid DataProductionClusterQuery query) {
		PageResult<DataProductionClusterVO> page = dataProductionClusterService.page(query);

		return Result.ok(page);
	}

	@GetMapping("{id}")
	@Operation(summary = "信息")
	@PreAuthorize("hasAuthority('data-development:cluster:info')")
	public Result<DataProductionClusterVO> get(@PathVariable("id") Long id) {
		DataProductionClusterEntity entity = dataProductionClusterService.getById(id);

		return Result.ok(DataProductionClusterConvert.INSTANCE.convert(entity));
	}

	@PostMapping
	@Operation(summary = "保存")
	@PreAuthorize("hasAuthority('data-development:cluster:save')")
	public Result<String> save(@RequestBody DataProductionClusterVO vo) {
		dataProductionClusterService.save(vo);
		return Result.ok();
	}

	@PutMapping
	@Operation(summary = "修改")
	@PreAuthorize("hasAuthority('data-development:cluster:update')")
	public Result<String> update(@RequestBody @Valid DataProductionClusterVO vo) {
		dataProductionClusterService.update(vo);

		return Result.ok();
	}

	@DeleteMapping
	@Operation(summary = "删除")
	@PreAuthorize("hasAuthority('data-development:cluster:delete')")
	public Result<String> delete(@RequestBody List<Long> idList) {
		dataProductionClusterService.delete(idList);

		return Result.ok();
	}

	@PostMapping("/heartbeat")
	@Operation(summary = "心跳检测")
	public Result<String> heartbeat(@RequestBody List<Long> idList) {
		dataProductionClusterService.heartbeat(idList);
		return Result.ok();
	}

	/**
	 * 回收过期集群
	 */
	@Operation(summary = "回收过期无效集群")
	@GetMapping("/clear")
	public Result<Integer> clear() {
		return Result.ok(dataProductionClusterService.clearCluster());
	}

	@GetMapping("/list-all")
	@Operation(summary = "获取全部集群实例")
	public Result<List<DataProductionClusterEntity>> listAll() {
		return Result.ok(dataProductionClusterService.listAll());
	}
}
