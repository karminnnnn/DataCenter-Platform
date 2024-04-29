package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceMetadataCollectRecordConvert;
import net.srt.entity.DataGovernanceMetadataCollectRecordEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.Result;
import net.srt.query.DataGovernanceMetadataCollectRecordQuery;
import net.srt.service.DataGovernanceMetadataCollectRecordService;
import net.srt.vo.DataGovernanceMetadataCollectRecordVO;
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
 * 数据治理-元数据采集任务记录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-04-04
 */
@RestController
@RequestMapping("metadata-collect-record")
@Tag(name = "数据治理-元数据采集任务记录")
@AllArgsConstructor
public class DataGovernanceMetadataCollectRecordController {
	private final DataGovernanceMetadataCollectRecordService dataGovernanceMetadataCollectRecordService;

	@GetMapping("page")
	@Operation(summary = "分页")
	public Result<PageResult<DataGovernanceMetadataCollectRecordVO>> page(@Valid DataGovernanceMetadataCollectRecordQuery query) {
		PageResult<DataGovernanceMetadataCollectRecordVO> page = dataGovernanceMetadataCollectRecordService.page(query);
		return Result.ok(page);
	}

	@GetMapping("{id}")
	@Operation(summary = "信息")
	public Result<DataGovernanceMetadataCollectRecordVO> get(@PathVariable("id") Long id) {
		DataGovernanceMetadataCollectRecordEntity entity = dataGovernanceMetadataCollectRecordService.getById(id);

		return Result.ok(DataGovernanceMetadataCollectRecordConvert.INSTANCE.convert(entity));
	}

	@DeleteMapping
	@Operation(summary = "删除")
	public Result<String> delete(@RequestBody List<Long> idList) {
		dataGovernanceMetadataCollectRecordService.delete(idList);

		return Result.ok();
	}
}
