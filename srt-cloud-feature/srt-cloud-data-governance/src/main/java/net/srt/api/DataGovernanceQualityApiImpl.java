package net.srt.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import net.srt.api.module.data.governance.DataMetadataCollectApi;
import net.srt.api.module.data.governance.DataQualityApi;
import net.srt.api.module.data.governance.constant.DbType;
import net.srt.api.module.data.governance.dto.DataGovernanceMetadataCollectDto;
import net.srt.api.module.data.governance.dto.DataGovernanceMetadataCollectRecordDto;
import net.srt.api.module.data.governance.dto.DataGovernanceMetadataDto;
import net.srt.api.module.data.governance.dto.DataGovernanceMetadataPropertyDto;
import net.srt.api.module.data.governance.dto.DataGovernanceQualityConfigDto;
import net.srt.api.module.data.governance.dto.DataGovernanceQualityTaskColumnDto;
import net.srt.api.module.data.governance.dto.DataGovernanceQualityTaskDto;
import net.srt.api.module.data.governance.dto.DataGovernanceQualityTaskTableDto;
import net.srt.convert.DataGovernanceMetadataCollectConvert;
import net.srt.convert.DataGovernanceMetadataCollectRecordConvert;
import net.srt.convert.DataGovernanceMetadataConvert;
import net.srt.convert.DataGovernanceMetadataPropertyConvert;
import net.srt.convert.DataGovernanceQualityConfigConvert;
import net.srt.convert.DataGovernanceQualityTaskColumnConvert;
import net.srt.convert.DataGovernanceQualityTaskConvert;
import net.srt.convert.DataGovernanceQualityTaskTableConvert;
import net.srt.entity.DataGovernanceMetadataCollectEntity;
import net.srt.entity.DataGovernanceMetadataCollectRecordEntity;
import net.srt.entity.DataGovernanceMetadataEntity;
import net.srt.entity.DataGovernanceMetadataPropertyEntity;
import net.srt.entity.DataGovernanceQualityTaskColumnEntity;
import net.srt.entity.DataGovernanceQualityTaskEntity;
import net.srt.entity.DataGovernanceQualityTaskTableEntity;
import net.srt.framework.common.utils.Result;
import net.srt.service.DataGovernanceMetadataCollectRecordService;
import net.srt.service.DataGovernanceMetadataCollectService;
import net.srt.service.DataGovernanceMetadataPropertyService;
import net.srt.service.DataGovernanceMetadataService;
import net.srt.service.DataGovernanceQualityConfigService;
import net.srt.service.DataGovernanceQualityTaskColumnService;
import net.srt.service.DataGovernanceQualityTaskService;
import net.srt.service.DataGovernanceQualityTaskTableService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName DataGovernanceQualityApiImpl
 * @Author zrx
 * @Date 2022/10/26 11:50
 */
@RestController
@RequiredArgsConstructor
public class DataGovernanceQualityApiImpl implements DataQualityApi {

	private final DataGovernanceQualityConfigService qualityConfigService;
	private final DataGovernanceQualityTaskService qualityTaskService;
	private final DataGovernanceQualityTaskTableService taskTableService;
	private final DataGovernanceQualityTaskColumnService taskColumnService;

	@Override
	public Result<DataGovernanceQualityConfigDto> getById(Long id) {
		return Result.ok(DataGovernanceQualityConfigConvert.INSTANCE.convertDto(qualityConfigService.getById(id)));
	}

	@Override
	public Result<DataGovernanceQualityTaskDto> addQualityTask(DataGovernanceQualityTaskDto qualityTaskDto) {
		DataGovernanceQualityTaskEntity entity = DataGovernanceQualityTaskConvert.INSTANCE.convert(qualityTaskDto);
		qualityTaskService.save(entity);
		qualityTaskDto.setId(entity.getId());
		return Result.ok(qualityTaskDto);
	}

	@Override
	public Result<String> updateQualityTask(DataGovernanceQualityTaskDto qualityTaskDto) {
		DataGovernanceQualityTaskEntity entity = DataGovernanceQualityTaskConvert.INSTANCE.convert(qualityTaskDto);
		qualityTaskService.updateById(entity);
		return Result.ok();
	}

	@Override
	public Result<DataGovernanceQualityTaskTableDto> addTaskTable(DataGovernanceQualityTaskTableDto qualityTaskTableDto) {
		DataGovernanceQualityTaskTableEntity entity = DataGovernanceQualityTaskTableConvert.INSTANCE.convert(qualityTaskTableDto);
		taskTableService.save(entity);
		qualityTaskTableDto.setId(entity.getId());
		return Result.ok(qualityTaskTableDto);
	}

	@Override
	public Result<String> updateQualityTaskTable(DataGovernanceQualityTaskTableDto taskTable) {
		DataGovernanceQualityTaskTableEntity entity = DataGovernanceQualityTaskTableConvert.INSTANCE.convert(taskTable);
		taskTableService.updateById(entity);
		return Result.ok();
	}

	@Override
	public Result<String> addQualityTaskColumns(List<DataGovernanceQualityTaskColumnDto> columnDtos) {
		List<DataGovernanceQualityTaskColumnEntity> columnEntities = DataGovernanceQualityTaskColumnConvert.INSTANCE.convertListByDtos(columnDtos);
		taskColumnService.saveBatch(columnEntities);
		return Result.ok();
	}
}
