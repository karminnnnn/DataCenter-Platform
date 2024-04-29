package net.srt.dto;

import lombok.Data;

/**
 * @ClassName CheckSqlResult
 * @Author zrx
 * @Date 2023/12/26 19:50
 */
@Data
public class CheckSqlResult {
	private boolean success;
	private String errorMsg;
}
