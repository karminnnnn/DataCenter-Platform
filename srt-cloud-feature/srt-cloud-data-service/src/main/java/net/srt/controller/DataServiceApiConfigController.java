package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataServiceApiConfigConvert;
import net.srt.dto.SqlDto;
import net.srt.entity.DataServiceApiConfigEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.service.DataServiceApiConfigService;
import net.srt.query.DataServiceApiConfigQuery;
import net.srt.vo.DataServiceApiAuthVO;
import net.srt.vo.DataServiceApiConfigVO;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import srt.cloud.framework.dbswitch.core.model.JdbcSelectResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 数据服务-api配置
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-01-28
 */
@RestController
@RequestMapping("/api-config")
@Tag(name = "数据服务-api配置")
@AllArgsConstructor
public class DataServiceApiConfigController {
	private final DataServiceApiConfigService dataServiceApiConfigService;

	@GetMapping("page")
	@Operation(summary = "分页")
	@PreAuthorize("hasAuthority('data-service:api-config:page')")
	public Result<PageResult<DataServiceApiConfigVO>> page(@Valid DataServiceApiConfigQuery query) {
		PageResult<DataServiceApiConfigVO> page = dataServiceApiConfigService.page(query);

		return Result.ok(page);
	}

	@GetMapping("page-resource")
	@Operation(summary = "根据资源id获取挂载的api列表")
	public Result<PageResult<DataServiceApiConfigVO>> pageResource(@Valid DataServiceApiConfigQuery query) {
		PageResult<DataServiceApiConfigVO> page = dataServiceApiConfigService.pageResource(query);

		return Result.ok(page);
	}

	@GetMapping("{id}")
	@Operation(summary = "信息")
	public Result<DataServiceApiConfigVO> get(@PathVariable("id") Long id) {
		DataServiceApiConfigEntity entity = dataServiceApiConfigService.getById(id);

		return Result.ok(DataServiceApiConfigConvert.INSTANCE.convert(entity));
	}

	@PostMapping
	@Operation(summary = "保存")
	@PreAuthorize("hasAuthority('data-service:api-config:save')")
	public Result<String> save(@RequestBody DataServiceApiConfigVO vo) {
		dataServiceApiConfigService.save(vo);

		return Result.ok();
	}

	@PutMapping
	@Operation(summary = "修改")
	@PreAuthorize("hasAuthority('data-service:api-config:update')")
	public Result<String> update(@RequestBody @Valid DataServiceApiConfigVO vo) {
		dataServiceApiConfigService.update(vo);

		return Result.ok();
	}

	@DeleteMapping
	@Operation(summary = "删除")
	@PreAuthorize("hasAuthority('data-service:api-config:delete')")
	public Result<String> delete(@RequestBody List<Long> idList) {
		dataServiceApiConfigService.delete(idList);

		return Result.ok();
	}

	@Operation(summary = "获取服务的api前缀地址")
	@GetMapping("/getIpPort")
	public Result<String> getIpPort() {
		return Result.ok(dataServiceApiConfigService.getIpPort());
	}

	@Operation(summary = "执行sql语句")
	@PostMapping("/sql/execute")
	public Result<JdbcSelectResult> sqlExecute(@RequestBody SqlDto sqlDto) {
		return Result.ok(dataServiceApiConfigService.sqlExecute(sqlDto));
	}

	/**
	 * 上线
	 */
	@Operation(summary = "上线")
	@PreAuthorize("hasAuthority('data-service:api-config:online')")
	@PutMapping(value = "/{id}/online")
	public Result<String> online(@PathVariable Long id) {
		dataServiceApiConfigService.online(id);
		return Result.ok();
	}

	/**
	 * 下线
	 */
	@Operation(summary = "下线")
	@PreAuthorize("hasAuthority('data-service:api-config:offline')")
	@PutMapping(value = "/{id}/offline")
	public Result<String> offline(@PathVariable Long id) {
		dataServiceApiConfigService.offline(id);
		return Result.ok();
	}

	@GetMapping("page-auth")
	@Operation(summary = "获取授权分页")
	public Result<PageResult<DataServiceApiConfigVO>> pageAuth(@Valid DataServiceApiConfigQuery query) {
		PageResult<DataServiceApiConfigVO> page = dataServiceApiConfigService.pageAuth(query);

		return Result.ok(page);
	}

	/**
	 * 获取授权信息
	 */
	@Operation(summary = "获取授权信息")
	@GetMapping(value = "/auth-info/{authId}")
	public Result<DataServiceApiAuthVO> getAuthInfo(@PathVariable Long authId) {
		return Result.ok(dataServiceApiConfigService.getAuthInfo(authId));
	}

	/**
	 * 导出 api 文档
	 */
	@Operation(summary = "导出 api 文档")
	@PostMapping(value = "/export-docs")
	public void exportDocs(@RequestBody List<Long> ids, HttpServletResponse response) {
		dataServiceApiConfigService.exportDocs(ids, response);
	}

	@Operation(summary = "获取服务的ip和端口号")
	@GetMapping("/ip-port")
	public Result<String> ipPort() {
		return Result.ok(dataServiceApiConfigService.ipPort());
	}

	@Operation(summary = "重置授权的调用次数")
	@PutMapping("/reset-requested/{authId}")
	public Result<String> resetRequested(@PathVariable Long authId) {
		dataServiceApiConfigService.resetRequested(authId);
		return Result.ok();
	}
}
