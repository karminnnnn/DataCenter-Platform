package net.srt.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据范围枚举
 */
@Getter
@AllArgsConstructor
public enum DataScopeEnum {
    /**
     * 全部数据
     */
    ALL(0),
    /**
     * 本部门数据
     */
    DEPT_ONLY(1),
    /**
     * 自定义数据
     */
    CUSTOM(2);

    private final Integer value;

}
