package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataServiceApiGroupConvert;
import net.srt.entity.DataServiceApiGroupEntity;
import net.srt.framework.common.utils.Result;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.service.DataServiceApiGroupService;
import net.srt.vo.DataServiceApiGroupVO;
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
* 数据服务-api分组
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-01-28
*/
@RestController
@RequestMapping("/api-group")
@Tag(name="数据服务-api分组")
@AllArgsConstructor
public class DataServiceApiGroupController {
    private final DataServiceApiGroupService dataServiceApiGroupService;


	@GetMapping
	@Operation(summary = "查询分组树")
	public Result<List<TreeNodeVo>> listTree() {
		return Result.ok(dataServiceApiGroupService.listTree());
	}

	@Deprecated
	@GetMapping("list-api-tree")
	@Operation(summary = "查询分组树-带api")
	public Result<List<TreeNodeVo>> listTreeWithApi() {
		return Result.ok(dataServiceApiGroupService.listTreeWithApi());
	}


    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('data-service:api-group:info')")
    public Result<DataServiceApiGroupVO> get(@PathVariable("id") Long id){
        DataServiceApiGroupEntity entity = dataServiceApiGroupService.getById(id);

        return Result.ok(DataServiceApiGroupConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('data-service:api-group:save')")
    public Result<String> save(@RequestBody DataServiceApiGroupVO vo){
        dataServiceApiGroupService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("hasAuthority('data-service:api-group:update')")
    public Result<String> update(@RequestBody @Valid DataServiceApiGroupVO vo){
        dataServiceApiGroupService.update(vo);

        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('data-service:api-group:delete')")
    public Result<String> delete(@PathVariable Long id){
        dataServiceApiGroupService.delete(id);

        return Result.ok();
    }
}
