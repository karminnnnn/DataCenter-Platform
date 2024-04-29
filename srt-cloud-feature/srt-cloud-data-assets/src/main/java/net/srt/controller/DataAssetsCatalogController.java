package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataAssetsCatalogConvert;
import net.srt.entity.DataAssetsCatalogEntity;
import net.srt.framework.common.utils.Result;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.service.DataAssetsCatalogService;
import net.srt.vo.DataAssetsCatalogVO;
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
* 数据资产-资产目录
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-07-04
*/
@RestController
@RequestMapping("/catalog")
@Tag(name="数据资产-资产目录")
@AllArgsConstructor
public class DataAssetsCatalogController {
    private final DataAssetsCatalogService dataAssetsCatalogService;


	@GetMapping("list-tree")
	@Operation(summary = "获取资产目录树")
	public Result<List<TreeNodeVo>> listTree() {
		List<TreeNodeVo> treeNodeVos = dataAssetsCatalogService.listTree();
		return Result.ok(treeNodeVos);
	}

    @GetMapping("{id}")
    @Operation(summary = "信息")
    public Result<DataAssetsCatalogVO> get(@PathVariable("id") Long id){
        DataAssetsCatalogEntity entity = dataAssetsCatalogService.getById(id);

        return Result.ok(DataAssetsCatalogConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    public Result<String> save(@RequestBody DataAssetsCatalogVO vo){
        dataAssetsCatalogService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    public Result<String> update(@RequestBody @Valid DataAssetsCatalogVO vo){
        dataAssetsCatalogService.update(vo);

        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除")
    public Result<String> delete(@PathVariable Long id){
        dataAssetsCatalogService.delete(id);

        return Result.ok();
    }
}
