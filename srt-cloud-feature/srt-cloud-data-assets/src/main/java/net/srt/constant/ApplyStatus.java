package net.srt.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 接入方式
 */
@Getter
@AllArgsConstructor
public enum ApplyStatus {
	/**
	 * 待审核
	 */
    WAIT_CHECK(0),
	/**
	 * 已通过
	 */
    PASS(1),
	/**
	 * 未通过
	 */
	NOT_PASS(2);

    private final Integer value;
}
