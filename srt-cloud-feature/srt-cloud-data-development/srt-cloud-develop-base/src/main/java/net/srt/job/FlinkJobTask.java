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

package net.srt.job;

import net.srt.entity.DataProductionTaskInstanceEntity;
import net.srt.flink.common.assertion.Asserts;
import net.srt.flink.common.context.SpringContextUtils;
import net.srt.flink.common.model.JobStatus;
import net.srt.flink.daemon.constant.FlinkTaskConstant;
import net.srt.flink.daemon.pool.DefaultThreadPool;
import net.srt.flink.daemon.task.DaemonTask;
import net.srt.flink.daemon.task.DaemonTaskConfig;
import net.srt.framework.common.utils.DateUtils;
import net.srt.service.DataProductionTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 后台进程 TODO
 */
@DependsOn("springContextUtils")
public class FlinkJobTask implements DaemonTask {

	private static final Logger log = LoggerFactory.getLogger(FlinkJobTask.class);

	private DaemonTaskConfig config;
	public static final String TYPE = "jobInstance";
	private static final DataProductionTaskService taskService;
	private long preDealTime;

	static {
		taskService = SpringContextUtils.getBeanByClass(DataProductionTaskService.class);
	}
	@Override
	public DaemonTask setConfig(DaemonTaskConfig config) {
		this.config = config;
		return this;
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public void dealTask() {
		long gap = System.currentTimeMillis() - this.preDealTime;
		if (gap < FlinkTaskConstant.TIME_SLEEP) {
			try {
				Thread.sleep(FlinkTaskConstant.TIME_SLEEP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		preDealTime = System.currentTimeMillis();
		DataProductionTaskInstanceEntity jobInstance = taskService.refreshJobInstance(config.getId(), false);
		if ((!JobStatus.isDone(jobInstance.getStatus())) || (Asserts.isNotNull(jobInstance.getFinishTime())
				&& Duration.between(DateUtils.asLocalDateTime(jobInstance.getFinishTime()), LocalDateTime.now()).toMinutes() < 1)) {
			DefaultThreadPool.getInstance().execute(this);
		} else {
			taskService.handleJobDone(jobInstance);
			FlinkJobTaskPool.getInstance().remove(config.getId().toString());
		}
	}
}
