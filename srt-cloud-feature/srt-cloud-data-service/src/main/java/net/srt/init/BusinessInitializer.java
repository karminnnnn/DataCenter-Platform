package net.srt.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.srt.entity.DataServiceApiConfigEntity;
import net.srt.handler.MappingHandlerMapping;
import net.srt.service.DataServiceApiConfigService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName BusinessInitializer
 * @Author zrx
 * @Date 2022/11/27 12:14
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BusinessInitializer implements ApplicationRunner {

	private final DataServiceApiConfigService apiConfigService;
	private final MappingHandlerMapping mappingHandlerMapping;

	@Override
	public void run(ApplicationArguments args) {
		log.info("init api service");
		//初始化注册已发布的api
		List<DataServiceApiConfigEntity> apiConfigEntities = apiConfigService.listActive();
		for (DataServiceApiConfigEntity api : apiConfigEntities) {
			mappingHandlerMapping.registerMapping(api);
		}
		log.info("init api service end");
	}
}
