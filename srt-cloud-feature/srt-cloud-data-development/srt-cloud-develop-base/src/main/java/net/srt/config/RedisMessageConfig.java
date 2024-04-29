package net.srt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @ClassName RedisMessageConfig
 * @Author zrx
 * @Date 2022/12/27 9:31
 */
@Configuration
public class RedisMessageConfig {

	public static final String DATA_PRO_SYS_CONFIG_TOPIC = "DATA_PRO_SYS_CONFIG_TOPIC";
	private static final String LISTEN_METHOD = "onMessage";

	@Bean
	public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapter, new PatternTopic(DATA_PRO_SYS_CONFIG_TOPIC));
		return container;
	}

	/**
	 * @return
	 */
	@Bean
	public MessageListenerAdapter listenerAdapter(RedisMessageListener listener) {
		return new MessageListenerAdapter(listener, LISTEN_METHOD);
	}

}
