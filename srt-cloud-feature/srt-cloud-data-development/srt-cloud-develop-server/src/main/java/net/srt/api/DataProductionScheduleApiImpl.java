package net.srt.api;

import lombok.RequiredArgsConstructor;
import net.srt.api.module.data.development.DataProductionScheduleApi;
import net.srt.api.module.data.development.constant.ExecuteType;
import net.srt.api.module.data.development.dto.DataProductionScheduleDto;
import net.srt.api.module.data.integrate.constant.CommonRunStatus;
import net.srt.convert.DataProductionScheduleConvert;
import net.srt.entity.DataProductionScheduleRecordEntity;
import net.srt.framework.common.utils.Result;
import net.srt.service.DataProductionScheduleRecordService;
import net.srt.service.DataProductionScheduleService;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName DataAccessApiImpl
 * @Author zrx
 * @Date 2022/10/26 11:50
 */
@RestController
@RequiredArgsConstructor
public class DataProductionScheduleApiImpl implements DataProductionScheduleApi {

	private final DataProductionScheduleService scheduleService;
	private final DataProductionScheduleRecordService recordService;

	@Override
	public Result<DataProductionScheduleDto> getById(Long id) {
		return Result.ok(DataProductionScheduleConvert.INSTANCE.convertDto(scheduleService.getById(id)));
	}

	@Override
	public Result<String> scheduleRun(Long id) {
		return Result.ok(scheduleService.scheduleRun(id.intValue(), ExecuteType.SCHEDULE));
	}

	@Override
	public Result<Boolean> scheduleComplete(Integer recordId) {
		DataProductionScheduleRecordEntity record = recordService.getById(recordId);
		//记录被删除了，返回true
		if (record == null) {
			return Result.ok(true);
		}
		return Result.ok(CommonRunStatus.SUCCESS.getCode().equals(record.getRunStatus()) || CommonRunStatus.FAILED.getCode().equals(record.getRunStatus()));
	}
}
