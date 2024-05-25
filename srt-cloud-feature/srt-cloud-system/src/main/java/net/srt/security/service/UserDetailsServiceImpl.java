package net.srt.security.service;

import lombok.AllArgsConstructor;
import net.srt.system.dao.SysUserDao;
import net.srt.system.entity.SysUserEntity;
import net.srt.system.service.SysUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
* @description: UserDetailsService实现，登录逻辑
* @author PatrickLi 373595331@qq.com
* @date 2024/5/21
*/
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final SysUserDetailsService sysUserDetailsService;
    private final SysUserDao sysUserDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserEntity userEntity = sysUserDao.getByUsername(username);
        // 用户不存在抛出异常
        if (userEntity == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        // 用户存在则返回UserDetails对象
        return sysUserDetailsService.getUserDetails(userEntity);
    }

}
