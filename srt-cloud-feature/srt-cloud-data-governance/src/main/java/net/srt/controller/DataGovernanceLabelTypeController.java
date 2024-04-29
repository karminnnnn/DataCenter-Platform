package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceLabelTypeConvert;
import net.srt.entity.DataGovernanceLabelTypeEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataGovernanceLabelTypeQuery;
import net.srt.service.DataGovernanceLabelTypeService;
import net.srt.vo.DataGovernanceLabelTypeVO;
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
 * 数据治理-标签类型
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-12-24
 */
@RestController
@RequestMapping("/label-type")
@Tag(name = "数据治理-标签类型")
@AllArgsConstructor
public class DataGovernanceLabelTypeController {
	private final DataGovernanceLabelTypeService dataGovernanceLabelTypeService;

	@GetMapping("page")
	@Operation(summary = "分页")
	@PreAuthorize("hasAuthority('data-governance:label-type:page')")
	public Result<PageResult<DataGovernanceLabelTypeVO>> page(@Valid DataGovernanceLabelTypeQuery query) {
		PageResult<DataGovernanceLabelTypeVO> page = dataGovernanceLabelTypeService.page(query);

		return Result.ok(page);
	}

	@GetMapping("{id}")
	@Operation(summary = "信息")
	@PreAuthorize("hasAuthority('data-governance:label-type:info')")
	public Result<DataGovernanceLabelTypeVO> get(@PathVariable("id") Long id) {
		DataGovernanceLabelTypeEntity entity = dataGovernanceLabelTypeService.getById(id);

		return Result.ok(DataGovernanceLabelTypeConvert.INSTANCE.convert(entity));
	}

	@PostMapping
	@Operation(summary = "保存")
	@PreAuthorize("hasAuthority('data-governance:label-type:save')")
	public Result<String> save(@RequestBody DataGovernanceLabelTypeVO vo) {
		dataGovernanceLabelTypeService.save(vo);

		return Result.ok();
	}

	@PutMapping
	@Operation(summary = "修改")
	@PreAuthorize("hasAuthority('data-governance:label-type:update')")
	public Result<String> update(@RequestBody @Valid DataGovernanceLabelTypeVO vo) {
		dataGovernanceLabelTypeService.update(vo);

		return Result.ok();
	}

	@DeleteMapping
	@Operation(summary = "删除")
	@PreAuthorize("hasAuthority('data-governance:label-type:delete')")
	public Result<String> delete(@RequestBody List<Long> idList) {
		dataGovernanceLabelTypeService.delete(idList);

		return Result.ok();
	}
}
