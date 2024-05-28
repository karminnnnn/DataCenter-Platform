package net.srt.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataDatabaseConvert;
import net.srt.convert.DataSourceConvert;
import net.srt.entity.DataDatabaseEntity;
import net.srt.entity.DataSourceEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataDatabaseQuery;
import net.srt.service.DataDatabaseService;
import net.srt.vo.DataDatabaseVO;
import net.srt.vo.DataSourceVO;
import net.srt.vo.DatabaseTestOnlineVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("database")
@Tag(name = "数据集成-数据库管理")
@AllArgsConstructor
public class DataDatabaseController {
    private final DataDatabaseService DataDatabaseService;


    @GetMapping("page")
    @Operation(summary = "数据库分页")
    //@PreAuthorize("hasAuthority('data-integrate:datasource:page')")    应该是要权限管理，但是目前没想好要怎么管理
    public Result<PageResult<DataDatabaseVO>> page(@Valid DataDatabaseQuery query) {
        PageResult<DataDatabaseVO> page = DataDatabaseService.page(query);
        return Result.ok(page);
    }

    @DeleteMapping
    @Operation(summary = "删除数据库")
    //@PreAuthorize("hasAuthority('data-integrate:database:delete')")
    public Result<String> delete(@RequestBody Long id) {
        DataDatabaseService.delete(id);
        return Result.ok();
    }

    @GetMapping("{id}")
    @Operation(summary = "获取数据库信息")
    //@PreAuthorize("hasAuthority('data-integrate:database:info')")
    public Result<DataDatabaseVO> get(@PathVariable("id") Long id) {
        DataDatabaseVO vo = DataDatabaseService.get(id);

        return Result.ok(vo);
    }

    @PostMapping
    @Operation(summary = "数据库保存")
    //@PreAuthorize("hasAuthority('data-integrate:database:save')")
    public Result<String> save(@RequestBody DataDatabaseVO vo) {
        DataDatabaseService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "数据库修改")
    //@PreAuthorize("hasAuthority('data-integrate:database:update')")
    public Result<String> update(@RequestBody @Valid DataDatabaseVO vo) {
        DataDatabaseService.update(vo);

        return Result.ok();
    }

    @PostMapping("/test-online")
    @Operation(summary = "测试连接")
    public Result<String> testOnline(@RequestBody @Valid DatabaseTestOnlineVO vo) {
        DataDatabaseService.testOnline(vo.getDatasourceId(), vo.getDatabaseName());
        return Result.ok();
    }
}
