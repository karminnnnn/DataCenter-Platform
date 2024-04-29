package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceLabelModelConvert;
import net.srt.dto.CheckSqlRequest;
import net.srt.dto.CheckSqlResult;
import net.srt.entity.DataGovernanceLabelModelEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.service.DataGovernanceLabelModelService;
import net.srt.query.DataGovernanceLabelModelQuery;
import net.srt.vo.DataGovernanceLabelModelVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
* 数据治理-标签实体
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-12-24
*/
@RestController
@RequestMapping("/label-model")
@Tag(name="数据治理-标签实体")
@AllArgsConstructor
public class DataGovernanceLabelModelController {
    private final DataGovernanceLabelModelService dataGovernanceLabelModelService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('data-governance:label-model:page')")
    public Result<PageResult<DataGovernanceLabelModelVO>> page(@Valid DataGovernanceLabelModelQuery query){
        PageResult<DataGovernanceLabelModelVO> page = dataGovernanceLabelModelService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('data-governance:label-model:info')")
    public Result<DataGovernanceLabelModelVO> get(@PathVariable("id") Long id){
        DataGovernanceLabelModelEntity entity = dataGovernanceLabelModelService.getById(id);

        return Result.ok(DataGovernanceLabelModelConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('data-governance:label-model:save')")
    public Result<String> save(@RequestBody DataGovernanceLabelModelVO vo){
        dataGovernanceLabelModelService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("hasAuthority('data-governance:label-model:update')")
    public Result<String> update(@RequestBody @Valid DataGovernanceLabelModelVO vo){
        dataGovernanceLabelModelService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('data-governance:label-model:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        dataGovernanceLabelModelService.delete(idList);

        return Result.ok();
    }

	@PostMapping("check-sql")
	@Operation(summary = "检查sql")
	public Result<CheckSqlResult> checkSql(@RequestBody @Valid CheckSqlRequest checkSqlRequest){
		return Result.ok(dataGovernanceLabelModelService.checkSql(checkSqlRequest));
	}
}
