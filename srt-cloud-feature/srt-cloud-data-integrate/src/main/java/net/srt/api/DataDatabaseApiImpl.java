package net.srt.api;

import lombok.RequiredArgsConstructor;
import net.srt.api.module.data.integrate.DataSourceApi;
import net.srt.api.module.data.integrate.dto.DataSourceDto;
import net.srt.convert.DataSourceConvert;
import net.srt.framework.common.utils.Result;
import net.srt.service.DataDatabaseService;
import net.srt.service.DataSourceService;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName DataDatabaseApiImpl
 * @Author zrx
 * @Date 2022/10/26 11:50
 */
@RestController
@RequiredArgsConstructor
public class DataDatabaseApiImpl implements DataSourceApi {

	private final DataSourceService DataSourceService;
	private final DataDatabaseService DataDatabaseService;

	@Override
	public Result<DataSourceDto> getById(Long id) {
		return Result.ok(DataSourceConvert.INSTANCE.convertDto(DataSourceService.getById(id)));
	}

	@Override
	public Result<Long> getDatasourceIdbyDatabaseId(Long id){
		return  Result.ok(Long.valueOf(DataDatabaseService.getDatasourceIdByDatabaseId(id)));
	}

	@Override
	public Result<String> getDataBaseBamebyId(Long id){
	   return  Result.ok(DataSourceService.getDatabasenameByID(id));
	}
}