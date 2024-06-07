package net.srt.api.module.data.integrate;

import net.srt.api.ServerNames;
import net.srt.api.module.data.integrate.dto.DataDatabaseDto;
import net.srt.api.module.data.integrate.dto.DataSourceDto;
import net.srt.framework.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @ClassName DataAccessApi
 * @Author zrx
 * @Date 2022/10/26 11:39
 */
@FeignClient(name = ServerNames.DATA_INTEGRATE_NAME, contextId = "data-integrate-database")
public interface DataSourceApi {
	/**
	 * 根据id获取
	 */
	@GetMapping(value = "api/data/integrate/datasource/{id}")
	Result<DataSourceDto> getById(@PathVariable Long id);


	@GetMapping(value = "api/data/integrate/database/{id}")
	Result<Long> getDatasourceIdbyDatabaseId(@PathVariable Long id);

	@GetMapping(value = "api/data/integrate/databasename/{id}")
	Result<String> getDataBaseBamebyId(@PathVariable Long id);
}
