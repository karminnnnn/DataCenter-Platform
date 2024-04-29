package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataProductionJarConvert;
import net.srt.entity.DataProductionJarEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataProductionJarQuery;
import net.srt.service.DataProductionJarService;
import net.srt.vo.DataProductionJarVO;
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
 * 数据生产-jar管理
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-11-13
 */
@RestController
@RequestMapping("/jar")
@Tag(name = "数据生产-jar管理")
@AllArgsConstructor
public class DataProductionJarController {
	private final DataProductionJarService dataProductionJarService;

	@GetMapping("page")
	@Operation(summary = "分页")
	@PreAuthorize("hasAuthority('data-development:jar:page')")
	public Result<PageResult<DataProductionJarVO>> page(@Valid DataProductionJarQuery query) {
		PageResult<DataProductionJarVO> page = dataProductionJarService.page(query);

		return Result.ok(page);
	}

	@GetMapping("{id}")
	@Operation(summary = "信息")
	@PreAuthorize("hasAuthority('data-development:jar:info')")
	public Result<DataProductionJarVO> get(@PathVariable("id") Long id) {
		DataProductionJarEntity entity = dataProductionJarService.getById(id);

		return Result.ok(DataProductionJarConvert.INSTANCE.convert(entity));
	}

	@PostMapping
	@Operation(summary = "保存")
	@PreAuthorize("hasAuthority('data-development:jar:save')")
	public Result<String> save(@RequestBody DataProductionJarVO vo) {
		dataProductionJarService.save(vo);

		return Result.ok();
	}

	@PutMapping
	@Operation(summary = "修改")
	@PreAuthorize("hasAuthority('data-development:jar:update')")
	public Result<String> update(@RequestBody @Valid DataProductionJarVO vo) {
		dataProductionJarService.update(vo);

		return Result.ok();
	}

	@DeleteMapping
	@Operation(summary = "删除")
	@PreAuthorize("hasAuthority('data-development:jar:delete')")
	public Result<String> delete(@RequestBody List<Long> idList) {
		dataProductionJarService.delete(idList);

		return Result.ok();
	}

	@GetMapping("list/{jarRunType}")
	@Operation(summary = "列表")
	public Result<List<DataProductionJarVO>> listAll(@PathVariable Integer jarRunType) {
		return Result.ok(dataProductionJarService.listAllByRunType(jarRunType));
	}
}
