package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataServiceApiLogQuery;
import net.srt.service.DataServiceApiLogService;
import net.srt.vo.DataServiceApiLogVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
* 数据服务-api请求日志
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-02-22
*/
@RestController
@RequestMapping("log")
@Tag(name="数据服务-api请求日志")
@AllArgsConstructor
public class DataServiceApiLogController {
    private final DataServiceApiLogService dataServiceApiLogService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('data-service:log:page')")
    public Result<PageResult<DataServiceApiLogVO>> page(@Valid DataServiceApiLogQuery query){
        PageResult<DataServiceApiLogVO> page = dataServiceApiLogService.page(query);

        return Result.ok(page);
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('data-service:log:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        dataServiceApiLogService.delete(idList);

        return Result.ok();
    }
}
