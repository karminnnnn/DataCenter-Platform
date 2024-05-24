package net.srt.security.config;

import lombok.AllArgsConstructor;
import net.srt.framework.security.mobile.MobileUserDetailsService;
import net.srt.framework.security.mobile.MobileVerifyCodeService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * SpringSecurity 配置文件
 *
 * @author 阿沐 babamu@126.com
 */
@Configuration
@AllArgsConstructor
public class SecurityConfig {
    // 用于从持久化存储（如数据库）加载用户特定数据
    private final UserDetailsService userDetailsService;
    // 对密码进行加密和匹配验证
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
    * @description: 数据库认证方式
    * @author PatrickLi 373595331@qq.com
    * @date 2024/5/21
    */
    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        return daoAuthenticationProvider;
    }

    /**
    * @description: 返回AuthenticationManager对象，已配置好认证的细节
    * @author PatrickLi 373595331@qq.com
    * @date 2024/5/21
    */
    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> providerList = new ArrayList<>();
        // 添加dao认证方式
        providerList.add(daoAuthenticationProvider());

        ProviderManager providerManager = new ProviderManager(providerList);
        // 注册标准事件发布器
        providerManager.setAuthenticationEventPublisher(new DefaultAuthenticationEventPublisher(applicationEventPublisher));

        return providerManager;
    }

}
