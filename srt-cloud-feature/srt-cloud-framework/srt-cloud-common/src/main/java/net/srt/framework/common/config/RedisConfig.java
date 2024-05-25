package net.srt.framework.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
* @description: Redis配置
* @author PatrickLi 373595331@qq.com
* @date 2024/5/23
*/
@Configuration
public class RedisConfig {

    /**
     * 创建并配置Jackson2JsonRedisSerializer对象，用于序列化和反序列化JSON格式的数据到Redis中。
     * 这个方法不接受任何参数，配置完成后返回配置好的Jackson2JsonRedisSerializer实例。
     *
     * @return Jackson2JsonRedisSerializer<Object> 配置好的序列化器实例，可以用于RedisTemplate的配置中。
     */
    @Bean
    public Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer(){
        // 创建Jackson2JsonRedisSerializer实例，并指定序列化对象的类型为Object
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        // 创建并配置ObjectMapper，以控制JSON序列化和反序列化的细节
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY); // 设置所有属性可序列化
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL); // 启用默认类型推断

        // 将配置好的ObjectMapper设置到Jackson2JsonRedisSerializer中
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        return jackson2JsonRedisSerializer;
    }


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // Key HashKey使用String序列化
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());

        // Value HashValue使用Jackson2JsonRedisSerializer序列化
        template.setValueSerializer(jackson2JsonRedisSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer());

        template.setConnectionFactory(factory);
        return template;
    }
}
