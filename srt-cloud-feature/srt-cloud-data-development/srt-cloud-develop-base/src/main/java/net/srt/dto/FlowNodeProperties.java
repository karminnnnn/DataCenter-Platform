package net.srt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName FlowNodeProperties
 * @Author zrx
 * @Date 2023/1/15 12:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowNodeProperties {
	public static final Map<String, Object> SUCCESS_STYLE = new HashMap<String, Object>() {
		{
			put("border","3px solid #06c733");
		}
	};
	public static final Map<String, Object> FALIE_STYLE = new HashMap<String, Object>() {
		{
			put("border","3px solid #e30000");
		}
	};
	/**
	 * 库里的node的Id
	 */
	private Long id;
	private Integer nodeRecordId;
	private String name;
	private Integer taskId;
	private Integer weight;
	private Integer taskType;
	private String taskTypeVal;
	private String note;
	private Integer failGoOn;
	private Map<String, Object> style;
	 //CommonRunStatus
	private Integer runStatus = 1;
}
