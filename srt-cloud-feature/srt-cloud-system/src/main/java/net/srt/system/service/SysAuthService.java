package net.srt.system.service;

import net.srt.system.vo.SysAccountLoginVO;
import net.srt.system.vo.SysTokenVO;

/**
 * 权限认证服务
 *
 * @author 阿沐 babamu@126.com
 */
public interface SysAuthService {

    SysTokenVO loginByAccount(SysAccountLoginVO login);
    /**
     * 退出登录
     *
     * @param accessToken accessToken
     */
    void logout(String accessToken);
}
