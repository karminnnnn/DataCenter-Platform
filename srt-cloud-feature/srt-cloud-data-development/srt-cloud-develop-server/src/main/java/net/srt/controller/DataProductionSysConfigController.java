package net.srt.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataProductionSysConfigConvert;
import net.srt.entity.DataProductionSysConfigEntity;
import net.srt.framework.common.utils.Result;
import net.srt.service.DataProductionSysConfigService;
import net.srt.vo.DataProductionSysConfigVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * 数据生产-配置中心
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-26
 */
@RestController
@RequestMapping("sys-config")
@Tag(name = "数据生产-配置中心")
@AllArgsConstructor
public class DataProductionSysConfigController {
	private final DataProductionSysConfigService dataProductionSysConfigService;


	/**
	 * 获取所有配置
	 */
	@GetMapping("/getAll")
	public Result<Map<String, Object>> getAll() {
		return Result.ok(dataProductionSysConfigService.getAll());
	}

	/**
	 * 批量更新配置
	 */
	@PostMapping("/updateSysConfigByJson")
	public Result updateSysConfigByJson(@RequestBody JsonNode para) {
		dataProductionSysConfigService.updateSysConfigByJson(para);
		return Result.ok();
	}

	@GetMapping("{id}")
	@Operation(summary = "信息")
	public Result<DataProductionSysConfigVO> get(@PathVariable("id") Long id) {
		DataProductionSysConfigEntity entity = dataProductionSysConfigService.getById(id);
		return Result.ok(DataProductionSysConfigConvert.INSTANCE.convert(entity));
	}

	@PostMapping
	@Operation(summary = "保存")
	public Result<String> save(@RequestBody DataProductionSysConfigVO vo) {
		dataProductionSysConfigService.save(vo);

		return Result.ok();
	}

	@PutMapping
	@Operation(summary = "修改")
	public Result<String> update(@RequestBody @Valid DataProductionSysConfigVO vo) {
		dataProductionSysConfigService.update(vo);

		return Result.ok();
	}
}
