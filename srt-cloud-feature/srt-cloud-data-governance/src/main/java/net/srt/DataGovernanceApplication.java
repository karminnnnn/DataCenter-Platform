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
 * 数据治理微服务
 *
 * @author zrx 985134801@qq.com
 */
@EnableFeignClients
@EnableDiscoveryClient
@EnableScheduling
@EnableAsync
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class DataGovernanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataGovernanceApplication.class, args);
	}

}
