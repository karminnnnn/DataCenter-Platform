package net.srt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName SqlCheckResultDto
 * @Author zrx
 * @Date 2023/7/22 19:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqlCheckResultDto {
	private Boolean pass;
	private String msg;
}
