package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceQualityTaskConvert;
import net.srt.entity.DataGovernanceQualityTaskEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataGovernanceQualityTaskQuery;
import net.srt.service.DataGovernanceQualityTaskService;
import net.srt.vo.DataGovernanceQualityTaskVO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
* 数据治理-质量任务
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-06-23
*/
@RestController
@RequestMapping("/quality-task")
@Tag(name="数据治理-质量任务")
@AllArgsConstructor
public class DataGovernanceQualityTaskController {
    private final DataGovernanceQualityTaskService dataGovernanceQualityTaskService;

    @GetMapping("page")
    @Operation(summary = "分页")
    public Result<PageResult<DataGovernanceQualityTaskVO>> page(@Valid DataGovernanceQualityTaskQuery query){
        PageResult<DataGovernanceQualityTaskVO> page = dataGovernanceQualityTaskService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    public Result<DataGovernanceQualityTaskVO> get(@PathVariable("id") Long id){
        DataGovernanceQualityTaskEntity entity = dataGovernanceQualityTaskService.getById(id);

        return Result.ok(DataGovernanceQualityTaskConvert.INSTANCE.convert(entity));
    }

    @DeleteMapping
    @Operation(summary = "删除")
    public Result<String> delete(@RequestBody List<Long> idList){
        dataGovernanceQualityTaskService.delete(idList);

        return Result.ok();
    }
}
