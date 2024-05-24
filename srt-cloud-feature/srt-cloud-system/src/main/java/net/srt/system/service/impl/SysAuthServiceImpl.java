package net.srt.system.service.impl;

import lombok.AllArgsConstructor;
import net.srt.framework.common.constant.Constant;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.security.cache.TokenStoreCache;
import net.srt.framework.security.user.UserDetail;
import net.srt.framework.security.utils.TokenUtils;
import net.srt.system.enums.LoginOperationEnum;
import net.srt.system.service.SysAuthService;
import net.srt.system.service.SysCaptchaService;
import net.srt.system.service.SysLogLoginService;
import net.srt.system.vo.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * @author PatrickLi 373595331@qq.com
 * @description: 权限认证服务
 * @date 2024/5/13
 */
@Service
@AllArgsConstructor
public class SysAuthServiceImpl implements SysAuthService {
    private final SysCaptchaService sysCaptchaService;
    private final TokenStoreCache tokenStoreCache;
    private final AuthenticationManager authenticationManager;
    private final SysLogLoginService sysLogLoginService;

    @Override
    public SysTokenVO loginByAccount(SysAccountLoginVO login) {
        // 验证码效验
        boolean flag = sysCaptchaService.validate(login.getKey(), login.getCaptcha());
        if (!flag) {
            // 保存登录日志
            sysLogLoginService.save(login.getUsername(), Constant.FAIL, LoginOperationEnum.CAPTCHA_FAIL.getValue());

            throw new ServerException("验证码错误");
        }

        Authentication authentication;
        try {
            // 用户认证
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
        } catch (BadCredentialsException e) {
            throw new ServerException("用户名或密码错误");
        }

        // 用户信息
        UserDetail user = (UserDetail) authentication.getPrincipal();

		/*
		//判断是否有项目id列表，没有，禁止登录
		if (!SuperAdminEnum.YES.getValue().equals(user.getSuperAdmin()) && CollectionUtils.isEmpty(user.getProjectIds())) {
			throw new ServerException("您没有项目租户空间可用，请联系管理员分配项目空间！");
		}
		*/

        // 生成 accessToken
        String accessToken = TokenUtils.generator();

        // 保存用户信息到缓存
        tokenStoreCache.saveUser(accessToken, user);

        return new SysTokenVO(accessToken);
    }

    @Override
    public SysTokenVO registerAccount(SysAccountRegisterVo register) {
        // 验证码效验
        boolean flag = sysCaptchaService.validate(register.getKey(), register.getCaptcha());
        if (!flag) {
            // TODO
            // 保存注册日志
            // sysLogLoginService.save(register.getUsername(), Constant.FAIL, LoginOperationEnum.CAPTCHA_FAIL.getValue());
            // throw new ServerException("验证码错误");
        }

        //存储用户的认证信息,代表了一个用户的认证状态
        Authentication authentication;
        try {
            // 调用用户认证接口，UsernamePasswordAuthenticationToken封装用户的身份信息和凭据
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    register.getUsername(), register.getPassword());
            // 实际调用的是实现AuthenticationManager的ProviderManager类的authenticate方法
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new ServerException("用户名或密码错误");
        }

        // 用户信息
        UserDetail user = (UserDetail) authentication.getPrincipal();

        // 生成 accessToken
        String accessToken = TokenUtils.generator();

        // 保存用户信息到缓存
        tokenStoreCache.saveUser(accessToken, user);

        return new SysTokenVO(accessToken);
    }

    @Override
    public void logout(String accessToken) {
        // 用户信息
        UserDetail user = tokenStoreCache.getUser(accessToken);

        // 删除用户信息
        tokenStoreCache.deleteUser(accessToken);

        // 保存登录日志
        sysLogLoginService.save(user.getUsername(), Constant.SUCCESS, LoginOperationEnum.LOGOUT_SUCCESS.getValue());
    }
}
