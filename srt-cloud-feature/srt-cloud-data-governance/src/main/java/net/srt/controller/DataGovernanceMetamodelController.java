package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceMetamodelConvert;
import net.srt.entity.DataGovernanceMetamodelEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.service.DataGovernanceMetamodelService;
import net.srt.vo.DataGovernanceMetamodelPropertyVO;
import net.srt.vo.DataGovernanceMetamodelVO;
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
 * 数据治理-元模型
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-03-28
 */
@RestController
@RequestMapping("/metamodel")
@Tag(name = "数据治理-元模型")
@AllArgsConstructor
public class DataGovernanceMetamodelController {
	private final DataGovernanceMetamodelService dataGovernanceMetamodelService;

	@GetMapping("list-tree")
	@Operation(summary = "获取元模型列表")
	public Result<List<TreeNodeVo>> listTree() {
		List<TreeNodeVo> treeNodeVos = dataGovernanceMetamodelService.listTree();
		return Result.ok(treeNodeVos);
	}

	@GetMapping("{id}")
	@Operation(summary = "信息")
	public Result<DataGovernanceMetamodelVO> get(@PathVariable("id") Long id) {
		DataGovernanceMetamodelEntity entity = dataGovernanceMetamodelService.getById(id);
		return Result.ok(DataGovernanceMetamodelConvert.INSTANCE.convert(entity));
	}

	@PostMapping
	@Operation(summary = "保存")
	public Result<String> save(@RequestBody DataGovernanceMetamodelVO vo) {
		dataGovernanceMetamodelService.save(vo);

		return Result.ok();
	}

	@PutMapping
	@Operation(summary = "修改")
	public Result<String> update(@RequestBody @Valid DataGovernanceMetamodelVO vo) {
		dataGovernanceMetamodelService.update(vo);

		return Result.ok();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除")
	public Result<String> delete(@PathVariable Long id) {
		dataGovernanceMetamodelService.delete(id);

		return Result.ok();
	}
}
