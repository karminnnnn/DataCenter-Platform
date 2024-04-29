package net.srt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName SavepointDto
 * @Author zrx
 * @Date 2023/12/8 14:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavepointDto {
	private Integer instanceId;
	private Integer historyId;
	private String savePointType;
}
