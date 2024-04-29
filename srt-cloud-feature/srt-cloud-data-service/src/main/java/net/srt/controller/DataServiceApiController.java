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
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("api")
@Tag(name = "数据服务-api")
@AllArgsConstructor
public class DataServiceApiController {
	private final DataServiceAppService dataServiceAppService;

	@GetMapping("/token/generate")
	public Result<String> tokenGenerate(@RequestParam String appKey, @RequestParam String appSecret) {
		return Result.ok(dataServiceAppService.tokenGenerate(appKey, appSecret));
	}
}
