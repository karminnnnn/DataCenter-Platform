package net.srt.system.service;

import net.srt.system.vo.SysAccountLoginVO;
import net.srt.system.vo.SysAccountRegisterVo;
import net.srt.system.vo.SysTokenVO;

/**
 * 权限认证服务
 *
 * @author 阿沐 babamu@126.com
 */
public interface SysAuthService {

    SysTokenVO loginByAccount(SysAccountLoginVO login);
    SysTokenVO registerAccount(SysAccountRegisterVo register);
    void logout(String accessToken);
}
