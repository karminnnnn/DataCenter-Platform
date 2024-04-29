package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataProductionCatalogueConvert;
import net.srt.entity.DataProductionCatalogueEntity;
import net.srt.framework.common.utils.Result;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.service.DataProductionCatalogueService;
import net.srt.vo.DataProductionCatalogueVO;
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
* 数据生产目录
*
* @author zrx 985134801@qq.com
* @since 2.0.0 2022-11-27
*/
@RestController
@RequestMapping("catalogue")
@Tag(name="数据生产目录")
@AllArgsConstructor
public class DataProductionCatalogueController {
    private final DataProductionCatalogueService dataProductionCatalogueService;


	@GetMapping
	@Operation(summary = "查询作业树")
	public Result<List<TreeNodeVo>> listTree() {
		return Result.ok(dataProductionCatalogueService.listTree());
	}

    @GetMapping("{id}")
    @Operation(summary = "信息")
    public Result<DataProductionCatalogueVO> get(@PathVariable("id") Long id){
        DataProductionCatalogueEntity entity = dataProductionCatalogueService.getById(id);

        return Result.ok(DataProductionCatalogueConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    public Result<String> save(@RequestBody DataProductionCatalogueVO vo){
        dataProductionCatalogueService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    public Result<String> update(@RequestBody @Valid DataProductionCatalogueVO vo){
        dataProductionCatalogueService.update(vo);

        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除")
    public Result<String> delete(@PathVariable Long id){
        dataProductionCatalogueService.delete(id);

        return Result.ok();
    }
}
