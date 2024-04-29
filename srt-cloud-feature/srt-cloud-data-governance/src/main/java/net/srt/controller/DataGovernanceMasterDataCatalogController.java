package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceMasterDataCatalogConvert;
import net.srt.entity.DataGovernanceMasterDataCatalogEntity;
import net.srt.framework.common.utils.Result;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.service.DataGovernanceMasterDataCatalogService;
import net.srt.vo.DataGovernanceMasterDataCatalogVO;
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
* 数据治理-主数据目录
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-09-27
*/
@RestController
@RequestMapping("/master-data-catalog")
@Tag(name="数据治理-主数据目录")
@AllArgsConstructor
public class DataGovernanceMasterDataCatalogController {
    private final DataGovernanceMasterDataCatalogService dataGovernanceMasterDataCatalogService;

	@GetMapping("list-master-model-tree")
	@Operation(summary = "获取含主数据的目录树")
	public Result<List<TreeNodeVo>> listMasterModelTree() {
		List<TreeNodeVo> treeNodeVos = dataGovernanceMasterDataCatalogService.listMasterModelTree();
		return Result.ok(treeNodeVos);
	}


	@GetMapping("list-tree")
	@Operation(summary = "获取目录树")
	public Result<List<TreeNodeVo>> listTree() {
		List<TreeNodeVo> treeNodeVos = dataGovernanceMasterDataCatalogService.listTree();
		return Result.ok(treeNodeVos);
	}

	@GetMapping("{id}")
	@Operation(summary = "信息")
	public Result<DataGovernanceMasterDataCatalogVO> get(@PathVariable("id") Long id){
		DataGovernanceMasterDataCatalogEntity entity = dataGovernanceMasterDataCatalogService.getById(id);

		return Result.ok(DataGovernanceMasterDataCatalogConvert.INSTANCE.convert(entity));
	}

    @PostMapping
    @Operation(summary = "保存")
    public Result<String> save(@RequestBody DataGovernanceMasterDataCatalogVO vo){
        dataGovernanceMasterDataCatalogService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    public Result<String> update(@RequestBody @Valid DataGovernanceMasterDataCatalogVO vo){
        dataGovernanceMasterDataCatalogService.update(vo);

        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除")
    public Result<String> delete(@PathVariable Long id){
        dataGovernanceMasterDataCatalogService.delete(id);

        return Result.ok();
    }
}
