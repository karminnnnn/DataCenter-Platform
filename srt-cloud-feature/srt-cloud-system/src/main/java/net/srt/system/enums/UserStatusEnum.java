package net.srt.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
* @description: 用户是否启用
* @author PatrickLi 373595331@qq.com
* @date 2024/5/22
*/
@Getter
@AllArgsConstructor
public enum UserStatusEnum {
    /**
     * 停用
     */
    DISABLE(0),
    /**
     * 正常
     */
    ENABLED(1);

    private final int value;
}
