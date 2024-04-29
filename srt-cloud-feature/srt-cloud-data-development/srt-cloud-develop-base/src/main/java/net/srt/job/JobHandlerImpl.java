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

import lombok.extern.slf4j.Slf4j;
import net.srt.entity.DataProductionClusterEntity;
import net.srt.entity.DataProductionScheduleNodeRecordEntity;
import net.srt.entity.DataProductionTaskEntity;
import net.srt.entity.DataProductionTaskHistoryEntity;
import net.srt.entity.DataProductionTaskInstanceEntity;
import net.srt.entity.DataProductionTaskInstanceHistoryEntity;
import net.srt.flink.common.assertion.Asserts;
import net.srt.flink.common.context.SpringContextUtils;
import net.srt.flink.common.model.JobStatus;
import net.srt.flink.common.utils.JSONUtil;
import net.srt.flink.core.job.Job;
import net.srt.flink.core.job.JobContextHolder;
import net.srt.flink.core.job.JobHandler;
import net.srt.flink.daemon.task.DaemonFactory;
import net.srt.flink.daemon.task.DaemonTaskConfig;
import net.srt.flink.gateway.GatewayType;
import net.srt.flink.process.context.ProcessContextHolder;
import net.srt.flink.process.model.ProcessEntity;
import net.srt.framework.common.utils.DateUtils;
import net.srt.service.DataProductionClusterConfigurationService;
import net.srt.service.DataProductionClusterService;
import net.srt.service.DataProductionJarService;
import net.srt.service.DataProductionScheduleNodeRecordService;
import net.srt.service.DataProductionTaskHistoryService;
import net.srt.service.DataProductionTaskInstanceHistoryService;
import net.srt.service.DataProductionTaskInstanceService;
import net.srt.service.DataProductionTaskService;
import org.springframework.context.annotation.DependsOn;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * JobHandlerImpl TODO
 *
 * @author zrx
 * @since 2021/6/27 0:04
 */
@Slf4j
@DependsOn("springContextUtils")
public class JobHandlerImpl implements JobHandler {

	private static final DataProductionTaskService taskService;
	private static final DataProductionTaskHistoryService historyService;
	private static final DataProductionTaskInstanceService instanceService;
	private static final DataProductionClusterService clusterService;
	private static final DataProductionTaskInstanceHistoryService instanceHistoryService;
	private static final DataProductionClusterConfigurationService clusterConfigurationService;
	private static final DataProductionScheduleNodeRecordService nodeRecordService;
	private static final DataProductionJarService jarService;

	static {
		historyService = SpringContextUtils.getBeanByClass(DataProductionTaskHistoryService.class);
		clusterService = SpringContextUtils.getBeanByClass(DataProductionClusterService.class);
		clusterConfigurationService = SpringContextUtils.getBeanByClass(DataProductionClusterConfigurationService.class);
		jarService = SpringContextUtils.getBeanByClass(DataProductionJarService.class);
		instanceService = SpringContextUtils.getBeanByClass(DataProductionTaskInstanceService.class);
		instanceHistoryService = SpringContextUtils.getBeanByClass(DataProductionTaskInstanceHistoryService.class);
		taskService = SpringContextUtils.getBeanByClass(DataProductionTaskService.class);
		nodeRecordService = SpringContextUtils.getBeanByClass(DataProductionScheduleNodeRecordService.class);
	}

	@Override
	public boolean init() {
		ProcessEntity process = ProcessContextHolder.getProcess();
		Job job = JobContextHolder.getJob();
		//log.info("current Job:" + job.toString());
		DataProductionTaskHistoryEntity history = new DataProductionTaskHistoryEntity();
		history.setProjectId(process.getProjectId().intValue());
		history.setType(job.getType().getLongValue());
		if (job.isUseGateway()) {
			history.setClusterConfigurationId(job.getJobConfig().getClusterConfigurationId());
		} else {
			history.setClusterId(job.getJobConfig().getClusterId());
		}
		if (job.getNodeRecordId() != null) {
			DataProductionScheduleNodeRecordEntity nodeRecordEntity = nodeRecordService.getById(job.getNodeRecordId());
			history.setExecuteType(nodeRecordEntity.getExecuteType());
			history.setScheduleId(nodeRecordEntity.getTaskScheduleId());
			history.setScheduleNodeId(nodeRecordEntity.getScheduleNodeId());
			history.setScheduleRecordId(nodeRecordEntity.getScheduleRecordId());
			history.setScheduleNodeRecordId(nodeRecordEntity.getId().intValue());
		}
		history.setExecuteNo(job.getExecuteNo());
		history.setJobManagerAddress(job.getJobManagerAddress());
		history.setJobName(job.getJobConfig().getJobName());
		history.setSession(job.getJobConfig().getSession());
		history.setStatus(Job.JobStatus.RUNNING.ordinal());
		history.setStatement(job.getStatement());
		history.setStartTime(Date.from(job.getStartTime().atZone(ZoneId.systemDefault()).toInstant()));
		history.setTaskId(job.getJobConfig().getTaskId());
		DataProductionTaskEntity task = taskService.getById(history.getTaskId());
		history.setCreator(task.getCreator());
		history.setOrgId(task.getOrgId());
		history.setDialect(task.getDialect());
		history.setDatabaseId(task.getDatabaseId() != null ? task.getDatabaseId().intValue() : null);
		history.setSqlDbType(task.getSqlDbType());
		history.setConfigJson(JSONUtil.toJsonString(job.getJobConfig()));
		historyService.save(history);
		job.setId(history.getId().intValue());
		return true;
	}

	@Override
	public boolean ready() {
		return true;
	}

	@Override
	public boolean running() {
		return true;
	}

	@Override
	public boolean success() {
		ProcessEntity process = ProcessContextHolder.getProcess();
		Job job = JobContextHolder.getJob();
		Integer taskId = job.getJobConfig().getTaskId();
		DataProductionTaskEntity task = taskService.getById(taskId);
		DataProductionTaskHistoryEntity history = new DataProductionTaskHistoryEntity();
		history.setCreator(task.getCreator());
		history.setOrgId(task.getOrgId());
		history.setId(job.getId().longValue());
		if (job.isUseGateway() && Asserts.isNullString(job.getJobId())) {
			job.setJobId("unknown");
			history.setStatus(Job.JobStatus.FAILED.ordinal());
			history.setJobId(job.getJobId());
			history.setEndTime(DateUtils.asDate(job.getEndTime()));
			history.setError(job.isEndByNoInsert() ? "insert语句集模式下没有检测到任何insert语句，实例运行失败" : "没有获取到任何JID，请自行排查原因");
			historyService.updateById(history);
			return false;
		}
		history.setStatus(job.getStatus().ordinal());
		history.setJobId(job.getJobId());
		//zrx
		history.setExecuteNo(job.getExecuteNo());
		history.setExecuteSql(job.getExecuteSql());
		history.setEndTime(DateUtils.asDate(job.getEndTime()));
		history.setJobManagerAddress(job.isUseGateway() ? job.getJobManagerAddress() : null);
		Integer clusterId = job.getJobConfig().getClusterId();
		DataProductionClusterEntity cluster;
		final Integer clusterConfigurationId = job.getJobConfig().getClusterConfigurationId();
		if (job.isUseGateway()) {
			DataProductionClusterEntity clusterEntity = DataProductionClusterEntity.autoRegistersCluster(
					job.getJobManagerAddress(),
					job.getJobId(),
					job.getJobConfig().getJobName() + "--" + LocalDateTime.now(),
					job.getType().getCode(),
					clusterConfigurationId,
					taskId);
			clusterEntity.setProjectId(process.getProjectId());
			clusterEntity.setOrgId(task.getOrgId());
			cluster = clusterService.registersCluster(clusterEntity);
			if (Asserts.isNotNull(cluster)) {
				clusterId = cluster.getId().intValue();
			}
		} else if (GatewayType.LOCAL.equalsValue(job.getJobConfig().getType())
				&& Asserts.isNotNullString(job.getJobManagerAddress()) && Asserts.isNotNull(job.getJobId())) {
			DataProductionClusterEntity clusterEntity = DataProductionClusterEntity.autoRegistersCluster(
					job.getJobManagerAddress(),
					job.getJobId(),
					job.getJobConfig().getJobName() + "--" + LocalDateTime.now(),
					job.getType().getCode(),
					null,
					taskId);
			clusterEntity.setProjectId(process.getProjectId());
			clusterEntity.setOrgId(task.getOrgId());
			cluster = clusterService.registersCluster(clusterEntity);
			if (Asserts.isNotNull(cluster)) {
				clusterId = cluster.getId().intValue();
			}
		} else {
			cluster = clusterService.getById(clusterId);
		}
		history.setClusterId(clusterId);
		historyService.updateById(history);
		//zrx 拉取结果
		historyService.pullResult(history);
		if (Asserts.isNullCollection(job.getJids()) || (GatewayType.LOCAL.equalsValue(job.getJobConfig().getType())
				&& Asserts.isNullString(job.getJobManagerAddress()))) {
			return true;
		}
		String jid = job.getJids().get(0);
		DataProductionTaskInstanceEntity jobInstance = history.buildJobInstance();
		jobInstance.setCreator(task.getCreator());
		jobInstance.setOrgId(task.getOrgId());
		jobInstance.setProjectId(history.getProjectId());
		jobInstance.setHistoryId(job.getId());
		jobInstance.setClusterId(clusterId);
		jobInstance.setTaskId(taskId);
		jobInstance.setName(job.getJobConfig().getJobName());
		jobInstance.setJid(jid);
		// zrx
		jobInstance.setExecuteNo(job.getExecuteNo());
		jobInstance.setExecuteSql(job.getExecuteSql());
		jobInstance.setStep(job.getJobConfig().getStep());
		jobInstance.setStatus(JobStatus.INITIALIZING.getValue());
		instanceService.save(jobInstance);

		job.setJobInstanceId(jobInstance.getId().intValue());
		task.setJobInstanceId(jobInstance.getId());
		taskService.updateById(task);

		DataProductionTaskInstanceHistoryEntity jobHistory = new DataProductionTaskInstanceHistoryEntity();
		jobHistory.setCreator(task.getCreator());
		jobHistory.setOrgId(task.getOrgId());
		jobHistory.setId(jobInstance.getId());
		jobHistory.setClusterJson(JSONUtil.toJsonString(cluster));
		jobHistory.setTaskId(taskId);
		jobHistory.setHistoryId(job.getId());

		jobHistory.setJarJson(Asserts.isNotNull(job.getJobConfig().getJarId())
				? JSONUtil.toJsonString(jarService.getById(job.getJobConfig().getJarId()))
				: null);

		jobHistory.setClusterConfigurationJson(Asserts.isNotNull(clusterConfigurationId)
				? JSONUtil.toJsonString(clusterConfigurationService.getClusterConfigById(clusterConfigurationId))
				: null);
		instanceHistoryService.save(jobHistory);
		//监控任务执行状态
		DaemonFactory.addTask(DaemonTaskConfig.build(FlinkJobTask.TYPE, jobInstance.getId().intValue()));
		return true;

	}

	@Override
	public boolean failed() {
		Job job = JobContextHolder.getJob();
		DataProductionTaskHistoryEntity history = new DataProductionTaskHistoryEntity();
		history.setId(job.getId().longValue());
		history.setExecuteSql(job.getExecuteSql());
		history.setJobId(job.getJobId());
		history.setStatus(job.getStatus().ordinal());
		history.setJobManagerAddress(job.getJobManagerAddress());
		history.setEndTime(DateUtils.asDate(job.getEndTime()));
		history.setError(job.getError());
		historyService.updateById(history);
		return true;
	}

	@Override
	public boolean callback() {
		return true;
	}

	@Override
	public boolean close() {
		return true;
	}
}
