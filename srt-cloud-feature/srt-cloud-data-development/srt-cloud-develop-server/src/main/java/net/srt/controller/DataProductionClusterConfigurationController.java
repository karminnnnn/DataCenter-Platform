package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataProductionClusterConfigurationConvert;
import net.srt.entity.DataProductionClusterConfigurationEntity;
import net.srt.flink.gateway.result.TestResult;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.service.DataProductionClusterConfigurationService;
import net.srt.query.DataProductionClusterConfigurationQuery;
import net.srt.vo.DataProductionClusterConfigurationVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
* 数据生产-集群配置
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2022-12-20
*/
@RestController
@RequestMapping("cluster-configuration")
@Tag(name="数据生产-集群配置")
@AllArgsConstructor
public class DataProductionClusterConfigurationController {
    private final DataProductionClusterConfigurationService dataProductionClusterConfigurationService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('data-development:cluster-configuration:page')")
    public Result<PageResult<DataProductionClusterConfigurationVO>> page(@Valid DataProductionClusterConfigurationQuery query){
        PageResult<DataProductionClusterConfigurationVO> page = dataProductionClusterConfigurationService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('data-development:cluster-configuration:info')")
    public Result<DataProductionClusterConfigurationVO> get(@PathVariable("id") Long id){
        DataProductionClusterConfigurationEntity entity = dataProductionClusterConfigurationService.getById(id);

        return Result.ok(DataProductionClusterConfigurationConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('data-development:cluster-configuration:save')")
    public Result<String> save(@RequestBody DataProductionClusterConfigurationVO vo){
        dataProductionClusterConfigurationService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("hasAuthority('data-development:cluster-configuration:update')")
    public Result<String> update(@RequestBody @Valid DataProductionClusterConfigurationVO vo){
        dataProductionClusterConfigurationService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('data-development:cluster-configuration:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        dataProductionClusterConfigurationService.delete(idList);

        return Result.ok();
    }

	/**
	 * 测试
	 */
	@Operation(summary = "测试")
	@PostMapping("/test-connect")
	public Result testConnect(@RequestBody DataProductionClusterConfigurationVO clusterConfiguration) {
		TestResult testResult = dataProductionClusterConfigurationService.testGateway(clusterConfiguration);
		if (testResult.isAvailable()) {
			return Result.ok();
		} else {
			throw new ServerException(testResult.getError());
		}
	}

	@GetMapping("list-all")
	@Operation(summary = "获取全部")
	public Result<List<DataProductionClusterConfigurationVO>> listAll(){
		List<DataProductionClusterConfigurationVO> list = dataProductionClusterConfigurationService.listAll();
		return Result.ok(list);
	}

}
