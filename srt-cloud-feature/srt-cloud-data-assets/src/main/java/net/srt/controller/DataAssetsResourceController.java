package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataAssetsResourceConvert;
import net.srt.entity.DataAssetsResourceEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataAssetsResourceQuery;
import net.srt.service.DataAssetsResourceService;
import net.srt.vo.DataAssetsResourceVO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
* 数据资产-资产列表
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-07-06
*/
@RestController
@RequestMapping("/resource")
@Tag(name="数据资产-资产列表")
@AllArgsConstructor
public class DataAssetsResourceController {
    private final DataAssetsResourceService dataAssetsResourceService;

    @GetMapping("page")
    @Operation(summary = "分页")
    public Result<PageResult<DataAssetsResourceVO>> page(@Valid DataAssetsResourceQuery query){
        PageResult<DataAssetsResourceVO> page = dataAssetsResourceService.page(query);

        return Result.ok(page);
    }

	@GetMapping("page-market")
	@Operation(summary = "数据集市分页")
	public Result<PageResult<DataAssetsResourceVO>> pageMarket(@Valid DataAssetsResourceQuery query){
		PageResult<DataAssetsResourceVO> page = dataAssetsResourceService.pageMarket(query);

		return Result.ok(page);
	}

    @GetMapping("{id}")
    @Operation(summary = "信息")
    public Result<DataAssetsResourceVO> get(@PathVariable("id") Long id){
        DataAssetsResourceEntity entity = dataAssetsResourceService.getById(id);

        return Result.ok(DataAssetsResourceConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    public Result<String> save(@RequestBody DataAssetsResourceVO vo){
        dataAssetsResourceService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    public Result<String> update(@RequestBody @Valid DataAssetsResourceVO vo){
        dataAssetsResourceService.update(vo);

        return Result.ok();
    }

	@PutMapping("/online/{id}")
	@Operation(summary = "上架")
	public Result<String> online(@PathVariable Long id){
		dataAssetsResourceService.online(id);

		return Result.ok();
	}

	@PutMapping("/offline/{id}")
	@Operation(summary = "下架")
	public Result<String> offline(@PathVariable Long id){
		dataAssetsResourceService.offline(id);

		return Result.ok();
	}

    @DeleteMapping("/{id}")
    @Operation(summary = "删除")
    public Result<String> delete(@PathVariable Long id){
        dataAssetsResourceService.delete(id);

        return Result.ok();
    }
}
