package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceQualityConfigCategoryConvert;
import net.srt.entity.DataGovernanceQualityConfigCategoryEntity;
import net.srt.framework.common.utils.Result;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.service.DataGovernanceQualityConfigCategoryService;
import net.srt.vo.DataGovernanceQualityConfigCategoryVO;
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
 * 数据治理-规则配置目录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-28
 */
@RestController
@RequestMapping("/quality-config-category")
@Tag(name = "数据治理-规则配置目录")
@AllArgsConstructor
public class DataGovernanceQualityConfigCategoryController {
	private final DataGovernanceQualityConfigCategoryService dataGovernanceQualityConfigCategoryService;


	@GetMapping("list-tree")
	@Operation(summary = "获取规则配置目录树")
	public Result<List<TreeNodeVo>> listTree() {
		List<TreeNodeVo> treeNodeVos = dataGovernanceQualityConfigCategoryService.listTree();
		return Result.ok(treeNodeVos);
	}

	@GetMapping("{id}")
	@Operation(summary = "信息")
	public Result<DataGovernanceQualityConfigCategoryVO> get(@PathVariable("id") Long id) {
		DataGovernanceQualityConfigCategoryEntity entity = dataGovernanceQualityConfigCategoryService.getById(id);

		return Result.ok(DataGovernanceQualityConfigCategoryConvert.INSTANCE.convert(entity));
	}

	@PostMapping
	@Operation(summary = "保存")
	public Result<String> save(@RequestBody DataGovernanceQualityConfigCategoryVO vo) {
		dataGovernanceQualityConfigCategoryService.save(vo);

		return Result.ok();
	}

	@PutMapping
	@Operation(summary = "修改")
	public Result<String> update(@RequestBody @Valid DataGovernanceQualityConfigCategoryVO vo) {
		dataGovernanceQualityConfigCategoryService.update(vo);

		return Result.ok();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除")
	public Result<String> delete(@PathVariable Long id) {
		dataGovernanceQualityConfigCategoryService.delete(id);

		return Result.ok();
	}
}
