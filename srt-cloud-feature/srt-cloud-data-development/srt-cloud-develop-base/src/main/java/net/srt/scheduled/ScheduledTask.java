package net.srt.scheduled;

import lombok.extern.slf4j.Slf4j;
import net.srt.flink.core.result.ResultPool;
import net.srt.flink.process.pool.ProcessPool;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时器:以下添加的定时器都是多线程异步执行的，互相之间没有任何干扰
 *
 * @author haojinlong
 * @date 2020/2/24 17:45
 */
@Component
@Slf4j
public class ScheduledTask {

	/**
	 * 每月1号3点定时清理内存（系统中已做了回收，主要怕异常导致部分内存没有回收导致泄露，每月一请即可）
	 */
	@Async("scheduledPoolTaskExecutor")
	@Scheduled(cron = "0 0 3 1 * ?")
	public void clearMemory() {
		try {
			//清空process
			//ProcessPool.clear();
			//清空console
			//ConsolePool.clear();
			//清空result
			ResultPool.clear();
		} catch (Exception e) {
			log.error("clear cache error", e);
		}

	}

}
