package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataMarketResourceCommentConvert;
import net.srt.entity.DataMarketResourceCommentEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataMarketResourceCommentQuery;
import net.srt.service.DataMarketResourceCommentService;
import net.srt.vo.DataMarketResourceCommentVO;
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
* 数据集市-资源评价表
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-08-27
*/
@RestController
@RequestMapping("/resource-comment")
@Tag(name="数据集市-资源评价表")
@AllArgsConstructor
public class DataMarketResourceCommentController {
    private final DataMarketResourceCommentService dataMarketResourceCommentService;

    @GetMapping("page")
    @Operation(summary = "分页")
    public Result<PageResult<DataMarketResourceCommentVO>> page(@Valid DataMarketResourceCommentQuery query){
        PageResult<DataMarketResourceCommentVO> page = dataMarketResourceCommentService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    public Result<DataMarketResourceCommentVO> get(@PathVariable("id") Long id){
        DataMarketResourceCommentEntity entity = dataMarketResourceCommentService.getById(id);

        return Result.ok(DataMarketResourceCommentConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    public Result<String> save(@RequestBody DataMarketResourceCommentVO vo){
        dataMarketResourceCommentService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    public Result<String> update(@RequestBody @Valid DataMarketResourceCommentVO vo){
        dataMarketResourceCommentService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    public Result<String> delete(@RequestBody List<Long> idList){
        dataMarketResourceCommentService.delete(idList);

        return Result.ok();
    }
}
