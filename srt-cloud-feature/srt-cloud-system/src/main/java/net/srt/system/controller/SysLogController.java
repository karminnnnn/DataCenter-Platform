package net.srt.system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.system.query.SysOtherLogQuery;
import net.srt.system.service.SysLogMetadataService;
import net.srt.system.service.SysLogService;
import net.srt.system.service.SysLogSysService;
import net.srt.system.service.SysLogUserService;
import net.srt.system.vo.SysLogVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("log")
@Tag(name = "其他日志")
@AllArgsConstructor
public class SysLogController {
    private final SysLogUserService sysLogUserService;
    private final SysLogSysService sysLogSysService;
    private final SysLogMetadataService sysLogMetadataService;
    @GetMapping("user/page")
    @Operation(summary = "分页")
    public Result<PageResult<SysLogVo>> pageUserActivity(@Valid SysOtherLogQuery query) {
        PageResult<SysLogVo> page = sysLogUserService.pageUser(query);

        return Result.ok(page);
    }

    @GetMapping("metadata/page")
    @Operation(summary = "分页")
    public Result<PageResult<SysLogVo>> pageMetadataActivity(@Valid SysOtherLogQuery query) {
        PageResult<SysLogVo> page = sysLogMetadataService.pageMetadata(query);

        return Result.ok(page);
    }


    @GetMapping("sys/page")
    @Operation(summary = "分页")
    public Result<PageResult<SysLogVo>> pageSysmanaActivity(@Valid SysOtherLogQuery query) {

        PageResult<SysLogVo> page = sysLogSysService.pageSys(query);

        return Result.ok(page);
    }
}