package net.srt.dto;

import lombok.Data;

/**
 * @ClassName CheckDto
 * @Author zrx
 * @Date 2023/8/18 16:07
 */
@Data
public class CheckDto {
	private Long id;
	private Integer status;
	private String checkNote;
}

