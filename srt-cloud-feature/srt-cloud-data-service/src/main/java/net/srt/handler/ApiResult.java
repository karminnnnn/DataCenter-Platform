package net.srt.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ResponseResult
 * @Author zrx
 * @Date 2023/2/15 12:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResult {
	private Boolean ifQuery;
	private String sql;
	private Long time;
	private Boolean success;
	private String errorMsg;
	private Integer affectedRows;
	private List<String> columns;
	private List<Map<String, Object>> rowData;
}
