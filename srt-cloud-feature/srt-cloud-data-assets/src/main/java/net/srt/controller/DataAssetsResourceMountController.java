package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.api.module.data.governance.constant.DbType;
import net.srt.convert.DataAssetsResourceMountConvert;
import net.srt.dto.SqlCheckResultDto;
import net.srt.dto.SqlExecuteDto;
import net.srt.entity.DataAssetsResourceMountEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.query.DataAssetsResourceMountQuery;
import net.srt.service.DataAssetsResourceMountService;
import net.srt.vo.DataAssetsResourceMountVO;
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
import java.util.Map;

/**
 * 数据资产-资源挂载表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-07-06
 */
@RestController
@RequestMapping("/resource-mount")
@Tag(name = "数据资产-资源挂载表")
@AllArgsConstructor
public class DataAssetsResourceMountController {
	private final DataAssetsResourceMountService dataAssetsResourceMountService;

	@GetMapping("page")
	@Operation(summary = "分页")
	public Result<PageResult<DataAssetsResourceMountVO>> page(@Valid DataAssetsResourceMountQuery query) {
		PageResult<DataAssetsResourceMountVO> page = dataAssetsResourceMountService.page(query);

		return Result.ok(page);
	}

	@GetMapping("{id}")
	@Operation(summary = "信息")
	public Result<DataAssetsResourceMountVO> get(@PathVariable("id") Long id) {
		DataAssetsResourceMountEntity entity = dataAssetsResourceMountService.getById(id);

		return Result.ok(DataAssetsResourceMountConvert.INSTANCE.convert(entity));
	}

	@GetMapping("/db-info/{resourceId}/{queryApply}")
	@Operation(summary = "根据资源id获取数据库表目录树")
	public Result<Map<DbType, List<TreeNodeVo>>> getDbInfo(@PathVariable("resourceId") Long resourceId, @PathVariable("queryApply") Integer queryApply) {
		return Result.ok(dataAssetsResourceMountService.getDbInfo(resourceId, queryApply));
	}

	/**
	 * 检查sql
	 */
	@PostMapping("/sql-check")
	@Operation(summary = "检查sql权限")
	public Result<SqlCheckResultDto> sqlCheck(@RequestBody SqlExecuteDto sqlExecuteDto) {
		return Result.ok(dataAssetsResourceMountService.sqlCheck(sqlExecuteDto));
	}

	@GetMapping("/check/{resourceId}")
	@Operation(summary = "检测挂载资源有效性")
	public Result<String> check(@PathVariable("resourceId") Long resourceId) {
		dataAssetsResourceMountService.check(resourceId);
		return Result.ok();
	}

	@PostMapping
	@Operation(summary = "保存")
	public Result<String> save(@RequestBody DataAssetsResourceMountVO vo) {
		dataAssetsResourceMountService.save(vo);

		return Result.ok();
	}

	@PutMapping
	@Operation(summary = "修改")
	public Result<String> update(@RequestBody @Valid DataAssetsResourceMountVO vo) {
		dataAssetsResourceMountService.update(vo);

		return Result.ok();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除")
	public Result<String> delete(@PathVariable Long id) {
		dataAssetsResourceMountService.delete(id);

		return Result.ok();
	}
}
