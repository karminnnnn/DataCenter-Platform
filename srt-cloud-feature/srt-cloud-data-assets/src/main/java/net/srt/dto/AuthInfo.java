package net.srt.dto;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName AuthIno
 * @Author zrx
 * @Date 2023/8/26 14:42
 */
@Data
public class AuthInfo {
	private Date startTime;
	private Date endTime;
	private Integer requestTImes;
	private Boolean hasActiveApply;
}
