package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.framework.common.utils.Result;
import net.srt.query.VisualizeInfo2Query;
import net.srt.service.VisualizeInfo2Service;
import net.srt.service.VisualizeInfo3Service;
import net.srt.service.VisualizeInfoService;
import net.srt.vo.VisualizeInfo2ListVO;
import net.srt.vo.VisualizeInfo3ListVO;
import net.srt.vo.VisualizeInfo3VO;
import net.srt.vo.VisualizeInfoListVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("data-visualization")
@Tag(name = "数据集成-可视化")
@AllArgsConstructor
public class VisualizeInfoController {
    private final VisualizeInfoService visualizeInfoService;
    private final VisualizeInfo2Service visualizeInfo2Service;
    private final VisualizeInfo3Service visualizeInfo3Service;
    @GetMapping("period-analysis")
    @Operation(summary = "业绩周期分析")
    public Result<VisualizeInfoListVO> get(){
        return Result.ok(visualizeInfoService.getVisualizeInfoList());
    }

    @GetMapping("study-analysis/page")
    @Operation(summary = "可视化接口")
    public Result<VisualizeInfo2ListVO> get2(@Valid VisualizeInfo2Query query){
        if(query.getNetId() == 0)
            return Result.ok(visualizeInfo2Service.getAll());
        return Result.ok(visualizeInfo2Service.getInfoByNetId(query.getNetId()));
    }

    @GetMapping("freshman-analysis")
    @Operation(summary = "学生属性分析")
    public Result<VisualizeInfo3ListVO> get3(){
        return Result.ok(visualizeInfo3Service.getVisualizeInfo3List());
    }
}
