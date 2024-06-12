package net.srt.api.module.system.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserActionEnum {
    INSERT(0),

    DELETE(1),

    UPDATE(2),

    SELECT(3);

    private final int value;
}
