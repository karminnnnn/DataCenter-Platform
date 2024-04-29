package net.srt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 数据资产微服务
 * 启动报 command too long 参考 https://blog.csdn.net/liumingzhe1/article/details/105413389?spm=1001.2101.3001.6661.1&utm_medium=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-105413389-blog-122864040.pc_relevant_3mothn_strategy_recovery&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-105413389-blog-122864040.pc_relevant_3mothn_strategy_recovery&utm_relevant_index=1
 *
 * @author zrx 985134801@qq.com
 */
@EnableAsync
@EnableScheduling
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class DataAssetsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataAssetsApplication.class, args);
	}

}
