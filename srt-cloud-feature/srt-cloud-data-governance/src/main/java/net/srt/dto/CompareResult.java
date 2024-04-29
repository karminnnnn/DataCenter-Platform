package net.srt.dto;

import lombok.Data;

/**
 * @ClassName CompareResult
 * @Author zrx
 * @Date 2023/5/27 10:16
 */
@Data
public class CompareResult {
	private String property;
	private String metadataVal;
	private String standardVal;
	private boolean standard;
}
