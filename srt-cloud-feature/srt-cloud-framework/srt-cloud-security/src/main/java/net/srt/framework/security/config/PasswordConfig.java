package net.srt.framework.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
* @description: 配置加密方法
* @author PatrickLi 373595331@qq.com
* @date 2024/5/21
*/
@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        /*
        * 工厂方法，返回一个 DelegatingPasswordEncoder 实例（实现了PasswordEncoder）。
        * 支持多种编码方案（如 bcrypt、noop、pbkdf2、scrypt 等），并且可以自动检测和使用密码的编码格式。
        * */
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
