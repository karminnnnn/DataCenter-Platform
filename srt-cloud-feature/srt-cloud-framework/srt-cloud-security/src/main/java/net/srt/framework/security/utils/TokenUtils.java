package net.srt.framework.security.utils;

import cn.hutool.core.lang.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
* @description: Token 工具类
* @author PatrickLi 373595331@qq.com
* @date 2024/5/21
*/
public class TokenUtils {

    /**
     * 生成 AccessToken
     */
    public static String generator() {
        // 生成带连字符的 UUID 字符串，标识信息的128位标识符，
        // 通常用于确保不同系统、不同时间生成的标识符是唯一的。
        return UUID.fastUUID().toString(true);
    }

    /**
     * 获取 AccessToken
     */
    public static String getAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (StringUtils.isBlank(accessToken)) {
            accessToken = request.getParameter("access_token");
        }
        return accessToken;
    }
}
