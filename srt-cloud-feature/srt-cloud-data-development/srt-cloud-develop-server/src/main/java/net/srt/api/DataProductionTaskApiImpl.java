package net.srt.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import net.srt.api.module.data.development.DataProductionTaskApi;
import net.srt.api.module.data.development.dto.DataProductionTaskDto;
import net.srt.convert.DataProductionTaskConvert;
import net.srt.entity.DataProductionTaskEntity;
import net.srt.framework.common.utils.Result;
import net.srt.service.DataProductionTaskService;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName DataAccessApiImpl
 * @Author zrx
 * @Date 2022/10/26 11:50
 */
@RestController
@RequiredArgsConstructor
public class DataProductionTaskApiImpl implements DataProductionTaskApi {

	private final DataProductionTaskService taskService;

	@Override
	public Result<DataProductionTaskDto> getByDbId(Long databaseId) {
		LambdaQueryWrapper<DataProductionTaskEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataProductionTaskEntity::getDatabaseId, databaseId).last(" limit 1");
		DataProductionTaskEntity one = taskService.getOne(wrapper);
		return Result.ok(DataProductionTaskConvert.INSTANCE.convertDto(one));
	}
}
