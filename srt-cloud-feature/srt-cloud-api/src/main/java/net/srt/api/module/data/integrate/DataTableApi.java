package net.srt.api.module.data.integrate;

import net.srt.api.ServerNames;
import net.srt.api.module.data.integrate.dto.DataTableDto;
import net.srt.framework.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @ClassName DataAccessApi
 * @Author zrx
 * @Date 2022/10/26 11:39
 */
@FeignClient(name = ServerNames.DATA_INTEGRATE_NAME, contextId = "data-integrate-data-table")
public interface DataTableApi {
	/**
	 * 添加ods
	 */
	@PostMapping(value = "api/data/integrate/ods")
	Result<String> addOds(@RequestBody DataTableDto dataTableDto);

	@PostMapping(value = "api/data/integrate/ods/{id}")
	Result<String> getdatatablenamebyID(@PathVariable Long id);

	@PostMapping(value = "api/data/integrate/ods/getid/{id}")
	Result<DataTableDto> getById(@PathVariable Long id);

	@PostMapping(value = "api/data/integrate/ods/getaccessid/{id}")
	Result<Long> getaccessidbydatabaseid(@PathVariable Long id);
}
