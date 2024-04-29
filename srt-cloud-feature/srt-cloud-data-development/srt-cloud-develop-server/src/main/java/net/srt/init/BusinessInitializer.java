/*
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package net.srt.init;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.srt.entity.DataProductionTaskInstanceEntity;
import net.srt.flink.daemon.task.DaemonFactory;
import net.srt.flink.daemon.task.DaemonTaskConfig;
import net.srt.job.FlinkJobTask;
import net.srt.service.DataProductionScheduleRecordService;
import net.srt.service.DataProductionScheduleService;
import net.srt.service.DataProductionSysConfigService;
import net.srt.service.DataProductionTaskInstanceService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * SystemInit
 *
 * @author wenmo
 * @since 2021/11/18
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessInitializer implements ApplicationRunner {

	private final DataProductionTaskInstanceService instanceService;
	private final DataProductionSysConfigService sysConfigService;
	private final DataProductionScheduleService scheduleService;

	@Override
	public void run(ApplicationArguments args) {
		sysConfigService.initSysConfig();
		initTaskMonitor();
		initScheduleMonitor();
	}

	/**
	 * init task monitor
	 */
	private void initTaskMonitor() {
		List<DataProductionTaskInstanceEntity> jobInstances = instanceService.listJobInstanceActive();
		List<DaemonTaskConfig> configList = new ArrayList<>();
		for (DataProductionTaskInstanceEntity jobInstance : jobInstances) {
			configList.add(new DaemonTaskConfig(FlinkJobTask.TYPE, jobInstance.getId().intValue()));
		}
		log.info("Number of tasks started: " + configList.size());
		DaemonFactory.start(configList);
	}

	/**
	 * init task monitor
	 */
	private void initScheduleMonitor() {
		//执行没有执行完的调度任务
		scheduleService.runActive();
	}

}
