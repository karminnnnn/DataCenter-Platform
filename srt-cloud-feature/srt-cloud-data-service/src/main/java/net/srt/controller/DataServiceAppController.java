package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataServiceAppConvert;
import net.srt.entity.DataServiceAppEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataServiceAppQuery;
import net.srt.service.DataServiceAppService;
import net.srt.vo.DataServiceApiAuthVO;
import net.srt.vo.DataServiceAppVO;
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
* 数据服务-app应用
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-02-16
*/
@RestController
@RequestMapping("app")
@Tag(name="数据服务-app应用")
@AllArgsConstructor
public class DataServiceAppController {
    private final DataServiceAppService dataServiceAppService;

    @GetMapping("page")
    @Operation(summary = "分页")
    //@PreAuthorize("hasAuthority('data-service:app:page')")
    public Result<PageResult<DataServiceAppVO>> page(@Valid DataServiceAppQuery query){
        PageResult<DataServiceAppVO> page = dataServiceAppService.page(query);
        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    //@PreAuthorize("hasAuthority('data-service:app:info')")
    public Result<DataServiceAppVO> get(@PathVariable("id") Long id){
        DataServiceAppEntity entity = dataServiceAppService.getById(id);

        return Result.ok(DataServiceAppConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    //@PreAuthorize("hasAuthority('data-service:app:save')")
    public Result<String> save(@RequestBody DataServiceAppVO vo){
        dataServiceAppService.save(vo);
        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    //@PreAuthorize("hasAuthority('data-service:app:update')")
    public Result<String> update(@RequestBody @Valid DataServiceAppVO vo){
        dataServiceAppService.update(vo);
        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    //@PreAuthorize("hasAuthority('data-service:app:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        dataServiceAppService.delete(idList);

        return Result.ok();
    }

	@PostMapping("/auth")
	@Operation(summary = "添加授权")
	@PreAuthorize("hasAuthority('data-service:app:auth')")
	public Result<String> addAuth(@RequestBody DataServiceApiAuthVO authVO){
		dataServiceAppService.addAuth(authVO);
		return Result.ok();
	}

	@PutMapping("/auth")
	@Operation(summary = "修改授权")
	@PreAuthorize("hasAuthority('data-service:app:auth')")
	public Result<String> upAuth(@RequestBody DataServiceApiAuthVO authVO){
		dataServiceAppService.upAuth(authVO);
		return Result.ok();
	}

	@DeleteMapping("/cancel-auth/{authId}")
	@Operation(summary = "取消授权")
	@PreAuthorize("hasAuthority('data-service:app:cancel-auth')")
	public Result<String> cancelAuth(@PathVariable Long authId){
		dataServiceAppService.cancelAuth(authId);
		return Result.ok();
	}
}
