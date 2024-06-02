package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.service.DataFieldService;
import net.srt.vo.DataFieldVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("ods")
@Tag(name = "数据集成-字段管理")
@AllArgsConstructor
public class DataFieldController {
    private final DataFieldService dataFieldService;

    @GetMapping("column-info/{fieldId}")
    @Operation(summary = "获取特定字段信息")
    public Result<DataFieldVO> getFieldInfo(@PathVariable("fieldId") Long fieldId) {
        DataFieldVO fieldInfo = dataFieldService.getFieldInfo(fieldId);
        return Result.ok(fieldInfo);
    }
}
