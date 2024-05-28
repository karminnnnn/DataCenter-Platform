package net.srt.dto;

import lombok.Data;
import net.srt.api.module.data.integrate.dto.DataDatabaseDto;
import net.srt.api.module.data.integrate.dto.DataSourceDto;

/**
 * @ClassName SqlConfigJson
 * @Author zrx
 * @Date 2023/1/2 20:21
 */
@Data
public class SqlConfigJson {
	private Integer sqlDbType;
	private DataSourceDto database;
	private Integer pvdataNum;
}
