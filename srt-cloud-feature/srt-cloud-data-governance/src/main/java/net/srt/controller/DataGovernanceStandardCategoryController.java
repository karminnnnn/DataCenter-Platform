package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceStandardCategoryConvert;
import net.srt.entity.DataGovernanceStandardCategoryEntity;
import net.srt.framework.common.utils.Result;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.service.DataGovernanceStandardCategoryService;
import net.srt.vo.DataGovernanceStandardCategoryVO;
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
* 数据治理-标准目录
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-05-05
*/
@RestController
@RequestMapping("/standard-category")
@Tag(name="数据治理-标准目录")
@AllArgsConstructor
public class DataGovernanceStandardCategoryController {
    private final DataGovernanceStandardCategoryService dataGovernanceStandardCategoryService;

	@GetMapping("list-tree")
	@Operation(summary = "获取数据标准目录树")
	public Result<List<TreeNodeVo>> listTree() {
		List<TreeNodeVo> treeNodeVos = dataGovernanceStandardCategoryService.listTree();
		return Result.ok(treeNodeVos);
	}

    @GetMapping("{id}")
    @Operation(summary = "信息")
    public Result<DataGovernanceStandardCategoryVO> get(@PathVariable("id") Long id){
        DataGovernanceStandardCategoryEntity entity = dataGovernanceStandardCategoryService.getById(id);

        return Result.ok(DataGovernanceStandardCategoryConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    public Result<String> save(@RequestBody DataGovernanceStandardCategoryVO vo){
        dataGovernanceStandardCategoryService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    public Result<String> update(@RequestBody @Valid DataGovernanceStandardCategoryVO vo){
        dataGovernanceStandardCategoryService.update(vo);

        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除")
    public Result<String> delete(@PathVariable Long id){
        dataGovernanceStandardCategoryService.delete(id);

        return Result.ok();
    }
}
