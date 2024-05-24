package net.srt.system.service;

import net.srt.system.entity.SysUserEntity;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 根据SysUserEntity转换为UserDetails 对象
 */
public interface SysUserDetailsService {
    UserDetails getUserDetails(SysUserEntity userEntity);
}
