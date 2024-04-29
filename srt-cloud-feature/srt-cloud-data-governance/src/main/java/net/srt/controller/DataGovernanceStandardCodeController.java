package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceStandardCodeConvert;
import net.srt.entity.DataGovernanceStandardCodeEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataGovernanceStandardCodeQuery;
import net.srt.service.DataGovernanceStandardCodeService;
import net.srt.vo.DataGovernanceStandardCodeVO;
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
* 数据治理-标准码表数据
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-05-19
*/
@RestController
@RequestMapping("/standard-code")
@Tag(name="数据治理-标准码表数据")
@AllArgsConstructor
public class DataGovernanceStandardCodeController {
    private final DataGovernanceStandardCodeService dataGovernanceStandardCodeService;

    @GetMapping("page")
    @Operation(summary = "分页")
    public Result<PageResult<DataGovernanceStandardCodeVO>> page(@Valid DataGovernanceStandardCodeQuery query){
        PageResult<DataGovernanceStandardCodeVO> page = dataGovernanceStandardCodeService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    public Result<DataGovernanceStandardCodeVO> get(@PathVariable("id") Long id){
        DataGovernanceStandardCodeEntity entity = dataGovernanceStandardCodeService.getById(id);

        return Result.ok(DataGovernanceStandardCodeConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    public Result<String> save(@RequestBody DataGovernanceStandardCodeVO vo){
        dataGovernanceStandardCodeService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    public Result<String> update(@RequestBody @Valid DataGovernanceStandardCodeVO vo){
        dataGovernanceStandardCodeService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    public Result<String> delete(@RequestBody List<Long> idList){
        dataGovernanceStandardCodeService.delete(idList);

        return Result.ok();
    }
}
