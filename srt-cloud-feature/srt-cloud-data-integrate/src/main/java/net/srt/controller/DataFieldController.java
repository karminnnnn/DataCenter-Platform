package net.srt.controller;

import cn.hutool.log.AbstractLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataFieldConvert;
import net.srt.entity.DataFieldEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataFieldQuery;
import net.srt.service.DataFieldService;
import net.srt.service.DataTableService;
import net.srt.vo.ColumnDescriptionVo;
import net.srt.vo.DataFieldVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static dm.jdbc.util.DriverUtil.log;

@RestController
@RequestMapping("ods")
@Tag(name = "数据集成-字段管理")
@AllArgsConstructor
public class DataFieldController {
    private final DataFieldService dataFieldService;

    @GetMapping("column-info/page")
    @Operation(summary = "字段信息分页")
    public Result<PageResult<ColumnDescriptionVo>> page(@Valid DataFieldQuery query) {
        PageResult<ColumnDescriptionVo> page = dataFieldService.page(query);
        return Result.ok(page);
    }

   /* @GetMapping("column-info/{fieldId}")
    @Operation(summary = "获取特定字段信息")
    public Result<DataFieldVO> getFieldInfo(@PathVariable("fieldId") Long fieldId) {
        log.debug("Received request to fetch field info with id: {}", String.valueOf(fieldId));
        DataFieldEntity entity = dataFieldService.getById(fieldId);
        if (entity == null) {
            log.warn("No data found for fieldId: {}", fieldId);
            return Result.error("No data found");
        }
        return Result.ok(DataFieldConvert.INSTANCE.convert(entity));
    }*/

    @PostMapping("column-info")
    @Operation(summary = "字段信息保存")
    public Result<String> save(@RequestBody @Valid DataFieldVO vo) {
        dataFieldService.save(vo);
        return Result.ok("Column info saved successfully");
    }

    @PutMapping("column-info")
    @Operation(summary = "字段信息修改")
    public Result<String> update(@RequestBody @Valid DataFieldVO vo) {
        dataFieldService.update(vo);
        return Result.ok("Column info updated successfully");
    }
    
    
    @DeleteMapping("column-info")
    @Operation(summary = "字段信息删除")
    public Result<String> delete(@RequestBody @Valid List<Long> idList) {
        dataFieldService.delete(idList);
        return Result.ok("Column info deleted successfully");
    }

    @GetMapping("/column-info/{fieldName}")
    @Operation(summary = "获取特定字段信息")
    public Result<ColumnDescriptionVo> columnInfo(@PathVariable("fieldName") String fieldName, @RequestParam Long datatableId) {
      //  Map<String, String> params = new HashMap<>();
      //  params.put("fieldName", fieldName);
      //  params.put("tableId", tableId);
      //  return Result.ok(params);
        Optional<ColumnDescriptionVo> columnInfoOpt = dataFieldService.getColumnInfo(fieldName, datatableId);

        if (columnInfoOpt.isPresent()) {
            return Result.ok(columnInfoOpt.get());
        } else {
            return Result.error("Field not found"); // 适当的错误处理
        }
    }

}
