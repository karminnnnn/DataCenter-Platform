package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataMarketResourceApplyConvert;
import net.srt.dto.CheckDto;
import net.srt.entity.DataMarketResourceApplyEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataMarketResourceApplyQuery;
import net.srt.service.DataMarketResourceApplyService;
import net.srt.vo.DataMarketResourceApplyVO;
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
 * 数据集市-资源申请表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-07-26
 */
@RestController
@RequestMapping("/resource-apply")
@Tag(name = "数据集市-资源申请表")
@AllArgsConstructor
public class DataMarketResourceApplyController {
	private final DataMarketResourceApplyService dataMarketResourceApplyService;

	@GetMapping("page")
	@Operation(summary = "分页")
	public Result<PageResult<DataMarketResourceApplyVO>> page(@Valid DataMarketResourceApplyQuery query) {
		PageResult<DataMarketResourceApplyVO> page = dataMarketResourceApplyService.page(query);

		return Result.ok(page);
	}

	@GetMapping("{id}")
	@Operation(summary = "信息")
	public Result<DataMarketResourceApplyVO> get(@PathVariable("id") Long id) {
		DataMarketResourceApplyEntity entity = dataMarketResourceApplyService.getById(id);

		return Result.ok(DataMarketResourceApplyConvert.INSTANCE.convert(entity));
	}

	@PostMapping
	@Operation(summary = "保存")
	public Result<String> save(@RequestBody DataMarketResourceApplyVO vo) {
		dataMarketResourceApplyService.save(vo);

		return Result.ok();
	}

	@PutMapping
	@Operation(summary = "修改")
	public Result<String> update(@RequestBody @Valid DataMarketResourceApplyVO vo) {
		dataMarketResourceApplyService.update(vo);

		return Result.ok();
	}

	@PutMapping("/check")
	@Operation(summary = "审核")
	public Result<String> check(@RequestBody CheckDto checkDto) {
		dataMarketResourceApplyService.check(checkDto);
		return Result.ok();
	}

	@PutMapping("/auth/{id}/{auth}")
	@Operation(summary = "授权")
	public Result<String> auth(@PathVariable Integer id, @PathVariable Integer auth) {
		dataMarketResourceApplyService.auth(id, auth);
		return Result.ok();
	}

	@DeleteMapping
	@Operation(summary = "删除")
	public Result<String> delete(@RequestBody List<Long> idList) {
		dataMarketResourceApplyService.delete(idList);

		return Result.ok();
	}
}
