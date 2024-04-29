package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataGovernanceQualityConfigQuery;
import net.srt.service.DataGovernanceQualityConfigService;
import net.srt.vo.DataGovernanceQualityConfigVO;
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
* 数据治理-质量规则配置
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-05-29
*/
@RestController
@RequestMapping("/quality-config")
@Tag(name="数据治理-质量规则配置")
@AllArgsConstructor
public class DataGovernanceQualityConfigController {
    private final DataGovernanceQualityConfigService dataGovernanceQualityConfigService;

    @GetMapping("page")
    @Operation(summary = "分页")
    public Result<PageResult<DataGovernanceQualityConfigVO>> page(@Valid DataGovernanceQualityConfigQuery query){
        PageResult<DataGovernanceQualityConfigVO> page = dataGovernanceQualityConfigService.page(query);
        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    public Result<DataGovernanceQualityConfigVO> get(@PathVariable("id") Long id){
        return Result.ok(dataGovernanceQualityConfigService.get(id));
    }

    @PostMapping
    @Operation(summary = "保存")
    public Result<String> save(@RequestBody DataGovernanceQualityConfigVO vo){
        dataGovernanceQualityConfigService.save(vo);
        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    public Result<String> update(@RequestBody @Valid DataGovernanceQualityConfigVO vo){
        dataGovernanceQualityConfigService.update(vo);
        return Result.ok();
    }

	@PutMapping("/online/{id}")
	@Operation(summary = "启用")
	public Result<String> online(@PathVariable Long id){
		dataGovernanceQualityConfigService.online(id);
		return Result.ok();
	}

	@PutMapping("/offline/{id}")
	@Operation(summary = "关闭")
	public Result<String> offline(@PathVariable Long id){
		dataGovernanceQualityConfigService.offline(id);
		return Result.ok();
	}

	@PutMapping("/hand-run/{id}")
	@Operation(summary = "手动执行")
	public Result<String> handRun(@PathVariable Long id){
		dataGovernanceQualityConfigService.handRun(id);
		return Result.ok();
	}

    @DeleteMapping
    @Operation(summary = "删除")
    public Result<String> delete(@RequestBody List<Long> idList){
        dataGovernanceQualityConfigService.delete(idList);
        return Result.ok();
    }
}
