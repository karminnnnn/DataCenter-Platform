package net.srt.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.srt.framework.common.exception.ServerException;

/**
 * 接入方式
 */
@Getter
@AllArgsConstructor
public enum FlinkVersion {
	/**
	 * 手动
	 */
	FLINK114("1.14"),
	/**
	 * 调度
	 */
	FLINK116("1.16");

	private final String value;

	public static FlinkVersion getByValue(String value) {
		for (FlinkVersion flinkVersion : FlinkVersion.values()) {
			if (flinkVersion.value.equals(value)) {
				return flinkVersion;
			}
		}
		throw new ServerException("Unsupported Flink Version:" + value);
	}
}
