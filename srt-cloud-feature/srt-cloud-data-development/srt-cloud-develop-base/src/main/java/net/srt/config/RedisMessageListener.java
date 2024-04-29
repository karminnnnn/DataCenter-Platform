package net.srt.config;

import lombok.RequiredArgsConstructor;
import net.srt.service.DataProductionSysConfigService;
import org.springframework.stereotype.Component;

/**
 * @ClassName DataProductionSysConfigListener
 * @Author zrx
 * @Date 2022/12/27 9:32
 */
@Component
@RequiredArgsConstructor
public class RedisMessageListener {

	private final DataProductionSysConfigService sysConfigService;

	/**
	 * 接收订阅的消息
	 *
	 * @param message
	 */
	public void onMessage(String message) {
		//System.out.println("message:" + message);
		if (RedisMessageConfig.DATA_PRO_SYS_CONFIG_TOPIC.equals(message)) {
			sysConfigService.initSysConfig();
		}
	}
}
