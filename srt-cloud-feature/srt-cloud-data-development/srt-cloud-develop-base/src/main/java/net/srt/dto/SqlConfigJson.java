package net.srt.dto;

import lombok.Data;
import net.srt.api.module.data.integrate.dto.DataDatabaseDto;

/**
 * @ClassName SqlConfigJson
 * @Author zrx
 * @Date 2023/1/2 20:21
 */
@Data
public class SqlConfigJson {
	private Integer sqlDbType;
	private DataDatabaseDto database;
	private Integer pvdataNum;
}
