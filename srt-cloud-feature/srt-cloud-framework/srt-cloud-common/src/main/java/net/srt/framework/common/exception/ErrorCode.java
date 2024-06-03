package net.srt.framework.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误编码
 *
 * @author 阿沐 babamu@126.com
*/
@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNAUTHORIZED(401, "还未授权，不能访问"),// 当匿名用户(token不存在、错误)时访问auth.xml未排除界面时产生
    FORBIDDEN(403, "没有权限，禁止访问"),// 用户缺少权限发生
    INTERNAL_SERVER_ERROR(500, "服务器异常，请稍后再试"); // 代码逻辑处理异常

    private final int code;
    private final String msg;
}
