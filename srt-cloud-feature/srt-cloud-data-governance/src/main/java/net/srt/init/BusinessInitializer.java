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
import net.srt.service.DataGovernanceMetadataCollectRecordService;
import net.srt.service.DataGovernanceQualityTaskService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

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

	private final DataGovernanceMetadataCollectRecordService collectRecordService;
	private final DataGovernanceQualityTaskService qualityTaskService;

	@Override
	public void run(ApplicationArguments args) {
		initScheduleMonitor();
	}

	/**
	 * init task monitor
	 */
	private void initScheduleMonitor() {
		//处理没执行完的采集任务
		collectRecordService.dealNotFinished();
		//处理没执行完的质量检测任务
		qualityTaskService.dealNotFinished();
	}

}
