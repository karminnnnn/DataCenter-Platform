package net.srt.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import net.srt.api.FlinkApiFactory;
import net.srt.api.FlinkVersion;
import net.srt.api.module.data.integrate.DataSourceApi;
import net.srt.api.module.data.integrate.dto.DataDatabaseDto;
import net.srt.constant.SqlDbType;
import net.srt.convert.DataProductionTaskConvert;
import net.srt.dao.DataProductionCatalogueDao;
import net.srt.dao.DataProductionScheduleDao;
import net.srt.dao.DataProductionScheduleNodeDao;
import net.srt.dao.DataProductionTaskDao;
import net.srt.dao.DataProductionTaskStatementDao;
import net.srt.dto.AbstractStatementDto;
import net.srt.dto.SavepointDto;
import net.srt.dto.SqlConfigJson;
import net.srt.dto.SqlDto;
import net.srt.dto.StudioExecuteDto;
import net.srt.entity.DataProductionCatalogueEntity;
import net.srt.entity.DataProductionClusterEntity;
import net.srt.entity.DataProductionJarEntity;
import net.srt.entity.DataProductionScheduleEntity;
import net.srt.entity.DataProductionScheduleNodeEntity;
import net.srt.entity.DataProductionScheduleNodeRecordEntity;
import net.srt.entity.DataProductionTaskEntity;
import net.srt.entity.DataProductionTaskHistoryEntity;
import net.srt.entity.DataProductionTaskInstanceEntity;
import net.srt.entity.DataProductionTaskInstanceHistoryEntity;
import net.srt.entity.DataProductionTaskSavepointsEntity;
import net.srt.entity.DataProductionTaskStatementEntity;
import net.srt.flink.common.assertion.Asserts;
import net.srt.flink.common.config.Dialect;
import net.srt.flink.common.model.JobLifeCycle;
import net.srt.flink.common.model.JobStatus;
import net.srt.flink.common.model.ProjectSystemConfiguration;
import net.srt.flink.common.model.SystemConfiguration;
import net.srt.flink.common.result.SqlExplainResult;
import net.srt.flink.common.utils.JSONUtil;
import net.srt.flink.common.utils.LogUtil;
import net.srt.flink.common.utils.RunTimeUtil;
import net.srt.flink.core.constant.FlinkRestResultConstant;
import net.srt.flink.core.job.Job;
import net.srt.flink.core.job.JobConfig;
import net.srt.flink.core.job.JobManager;
import net.srt.flink.core.job.JobResult;
import net.srt.flink.core.result.SelectResult;
import net.srt.flink.daemon.task.DaemonFactory;
import net.srt.flink.daemon.task.DaemonTaskConfig;
import net.srt.flink.executor.constant.FlinkConstant;
import net.srt.flink.gateway.Gateway;
import net.srt.flink.gateway.GatewayType;
import net.srt.flink.gateway.config.GatewayConfig;
import net.srt.flink.gateway.config.SavePointType;
import net.srt.flink.gateway.model.JobInfo;
import net.srt.flink.gateway.result.SavePointResult;
import net.srt.flink.process.context.ProcessContextHolder;
import net.srt.flink.process.model.ProcessEntity;
import net.srt.flink.process.model.ProcessType;
import net.srt.flink.process.pool.ConsolePool;
import net.srt.flink.process.pool.ProcessPool;
import net.srt.framework.common.cache.bean.DataProjectCacheBean;
import net.srt.framework.common.config.Config;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.DateUtils;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.job.FlinkJobTask;
import net.srt.job.FlinkJobTaskPool;
import net.srt.model.ConsoleLog;
import net.srt.model.JobInfoDetail;
import net.srt.query.DataProductionTaskHistoryQuery;
import net.srt.query.DataProductionTaskSavepointsQuery;
import net.srt.service.DataProductionClusterConfigurationService;
import net.srt.service.DataProductionClusterService;
import net.srt.service.DataProductionJarService;
import net.srt.service.DataProductionTaskHistoryService;
import net.srt.service.DataProductionTaskInstanceHistoryService;
import net.srt.service.DataProductionTaskInstanceService;
import net.srt.service.DataProductionTaskSavepointsService;
import net.srt.service.DataProductionTaskService;
import net.srt.vo.DataProductionTaskHistoryVO;
import net.srt.vo.DataProductionTaskInstanceHistoryVO;
import net.srt.vo.DataProductionTaskSavepointsVO;
import net.srt.vo.DataProductionTaskVO;
import org.apache.flink.configuration.RestOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.type.ProductTypeEnum;
import srt.cloud.framework.dbswitch.common.util.SingletonObject;
import srt.cloud.framework.dbswitch.common.util.StringUtil;
import srt.cloud.framework.dbswitch.core.model.JdbcSelectResult;
import srt.cloud.framework.dbswitch.core.service.IMetaDataByJdbcService;
import srt.cloud.framework.dbswitch.core.service.impl.MetaDataByJdbcServiceImpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 数据生产任务
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-05
 */
@Service
@AllArgsConstructor
public class DataProductionTaskServiceImpl extends BaseServiceImpl<DataProductionTaskDao, DataProductionTaskEntity> implements DataProductionTaskService {

	private final DataProductionTaskStatementDao statementDao;
	private final DataProductionCatalogueDao catalogueDao;
	private final DataProductionClusterService clusterService;
	private final DataSourceApi DataSourceApi;
	private final DataProductionTaskInstanceService instanceService;
	private final DataProductionTaskInstanceHistoryService instanceHistoryService;
	private final DataProductionTaskHistoryService historyService;
	private final DataProductionClusterConfigurationService clusterConfigurationService;
	private final DataProductionTaskSavepointsService savepointsService;
	private final DataProductionScheduleNodeDao scheduleNodeDao;
	private final DataProductionScheduleDao scheduleDao;
	private final DataProductionJarService jarService;
	private final Config config;

	private String buildParas(Integer id) {
		return buildParas(id, StrUtil.NULL);
	}

	private String buildParas(Integer id, String flinkyAddr) {
		return "--id " + id + " --driver " + config.getDriver() + " --url " + config.getUrl() + " --username " + config.getUsername() + " --password "
				+ config.getPassword() + " --flinkyAddr " + flinkyAddr;
	}

	@Override
	public DataProductionTaskVO save(DataProductionTaskVO vo) {
		DataProductionCatalogueEntity catalogueEntity = catalogueDao.selectById(vo.getCatalogueId());
		//目前只有两种类型，后续添加 UDF TODO
		DataProductionTaskEntity task = DataProductionTaskConvert.INSTANCE.convert(vo);
		task.setOrgId(catalogueEntity.getOrgId());
		task.setProjectId(getProjectId());
		task.setStep(JobLifeCycle.CREATE.getValue());
		task.setCheckPoint(task.getCheckPoint() == null ? 0 : task.getCheckPoint());
		task.setParallelism(task.getParallelism() == null ? 1 : task.getParallelism());
		baseMapper.insert(task);
		//如果不是FLinkJar任务，需要设置statement
		if (!Dialect.FLINKJAR.getCode().equals(task.getDialect())) {
			DataProductionTaskStatementEntity statement = new DataProductionTaskStatementEntity();
			statement.setTaskId(task.getId());
			statement.setProjectId(getProjectId());
			statement.setStatement(task.getStatement() == null ? "" : task.getStatement());
			statementDao.insert(statement);
		}
		//更新目录的taskId
		catalogueDao.updateTaskIdById(task.getId(), task.getCatalogueId());
		return DataProductionTaskConvert.INSTANCE.convert(task);
	}

	@Override
	public DataProductionTaskVO update(DataProductionTaskVO vo) {
		DataProductionTaskEntity task = DataProductionTaskConvert.INSTANCE.convert(vo);
		DataProductionTaskEntity taskInfo = getById(task.getId());
		if (JobLifeCycle.RELEASE.equalsValue(taskInfo.getStep())
				|| JobLifeCycle.ONLINE.equalsValue(taskInfo.getStep())
				|| JobLifeCycle.CANCEL.equalsValue(taskInfo.getStep())) {
			throw new ServerException("该作业已" + JobLifeCycle.get(taskInfo.getStep()).getLabel() + "，禁止修改！");
		}
		task.setOrgId(taskInfo.getOrgId());
		task.setProjectId(getProjectId());
		task.setStep(JobLifeCycle.DEVELOP.getValue());
		updateById(task);
		if (task.getStatement() != null && !Dialect.FLINKJAR.getCode().equals(task.getDialect())) {
			DataProductionTaskStatementEntity statement = new DataProductionTaskStatementEntity();
			statement.setTaskId(task.getId());
			statement.setProjectId(getProjectId());
			statement.setStatement(task.getStatement());
			statementDao.updataByTaskId(statement);
		}
		return DataProductionTaskConvert.INSTANCE.convert(task);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) {
		//判断是否有调度任务与之关联
		LambdaQueryWrapper<DataProductionScheduleNodeEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataProductionScheduleNodeEntity::getTaskId, id).last(" limit 1");
		DataProductionScheduleNodeEntity nodeEntity = scheduleNodeDao.selectOne(wrapper);
		if (nodeEntity != null) {
			DataProductionScheduleEntity scheduleEntity = scheduleDao.selectById(nodeEntity.getTaskScheduleId());
			throw new ServerException(String.format("存在调度流程【%s】的节点【%s】与之关联，不可删除！", scheduleEntity.getName(), nodeEntity.getName()));
		}
		removeById(id);
		//同步删除关联表
		Map<String, Object> deleteMap = new HashMap<>();
		deleteMap.put("task_id", id);
		statementDao.deleteByMap(deleteMap);
		historyService.removeByMap(deleteMap);
		instanceService.removeByMap(deleteMap);
		instanceHistoryService.removeByMap(deleteMap);
		savepointsService.removeByMap(deleteMap);
	}

	@Override
	public DataProductionTaskVO get(Long id) {
		DataProductionTaskEntity task = getById(id);
		LambdaQueryWrapper<DataProductionTaskStatementEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataProductionTaskStatementEntity::getTaskId, id).last(" limit 1");
		if (!Dialect.FLINKJAR.getCode().equals(task.getDialect())) {
			DataProductionTaskStatementEntity statement = statementDao.selectOne(wrapper);
			task.setStatement(statement == null ? "" : statement.getStatement());
		}
		return DataProductionTaskConvert.INSTANCE.convert(task);
	}

	@Override
	public List<SqlExplainResult> explainSql(StudioExecuteDto studioExecuteDto) {
		buildExecuteDto(studioExecuteDto);
		if (Dialect.notFlinkSql(studioExecuteDto.getDialect())) {
			return explainCommonSql(studioExecuteDto);
		} else {
			return explainFlinkSql(studioExecuteDto);
		}
	}

	@Override
	public JobResult executeSql(StudioExecuteDto studioExecuteDto) {
		buildExecuteDto(studioExecuteDto);
		if (Dialect.notFlinkSql(studioExecuteDto.getDialect())) {
			return executeCommonSql(SqlDto.build(
					studioExecuteDto.getTaskId(),
					studioExecuteDto.getStatement(),
					studioExecuteDto.getSqlDbType(),
					studioExecuteDto.getDatabaseId(),
					studioExecuteDto.getOpenTrans(),
					studioExecuteDto.getMaxRowNum()));
		} else {
			return executeFlinkSql(studioExecuteDto);
		}
	}

	@Override
	public JobResult justExecuteSql(StudioExecuteDto studioExecuteDto) {
		String processId = getAccessToken();
		ProcessEntity process = ProcessContextHolder.registerProcess(
				ProcessEntity.init(ProcessType.SQLEXECUTE, processId));
		JobResult result = new JobResult();
		result.setStatement(studioExecuteDto.getStatement());
		result.setStartTimeNow();
		process.info("Initializing database connection...");
		boolean ifMiddleDb = SqlDbType.MIDDLE_DB.getValue().equals(studioExecuteDto.getSqlDbType());
		DataDatabaseDto database;
		if (ifMiddleDb) {
			DataProjectCacheBean project = getProject(getProjectId());
			database = new DataDatabaseDto();
			database.setName(project.getName() + "<中台库>");
			database.setDatabaseName(project.getDbName());
			database.setDatabaseSchema(project.getDbSchema());
			database.setJdbcUrl(project.getDbUrl());
			database.setUserName(project.getDbUsername());
			database.setPassword(project.getDbPassword());
			database.setDatabaseType(project.getDbType());
		} else {
			database = DataSourceApi.getById(studioExecuteDto.getDatabaseId().longValue()).getData();
		}
		try {
			// zrx
			ProductTypeEnum productTypeEnum = ProductTypeEnum.getByIndex(database.getDatabaseType());
			IMetaDataByJdbcService metaDataService = new MetaDataByJdbcServiceImpl(productTypeEnum);
			process.start();
			String jdbcUrl = database.getJdbcUrl();
			String userName = database.getUserName();
			String password = database.getPassword();
			JdbcSelectResult selectResult = metaDataService.queryDataBySql(jdbcUrl, productTypeEnum.name(), userName, password, studioExecuteDto.getStatement(), studioExecuteDto.getOpenTrans(), studioExecuteDto.getMaxRowNum());
			process.finish("Execute sql succeed.");
			result.setResult(selectResult);
			result.setSuccess(selectResult.getSuccess());
			process.infoEnd();
			result.setEndTimeNow();
		} catch (Exception e) {
			result.setSuccess(false);
			process.error(LogUtil.getError(e));
			process.infoEnd();
			result.setEndTimeNow();
		} finally {
			ProcessContextHolder.clear();
		}
		return result;
	}

	@Override
	public DataProductionTaskInstanceEntity refreshJobInstance(Integer id, boolean isCoercive) {
		JobInfoDetail jobInfoDetail;
		FlinkJobTaskPool pool = FlinkJobTaskPool.getInstance();
		String key = id.toString();
		if (pool.exist(key)) {
			jobInfoDetail = pool.get(key);
		} else {
			jobInfoDetail = new JobInfoDetail(id);
			DataProductionTaskInstanceEntity jobInstance = instanceService.getByIdWithoutTenant(id);
			Asserts.checkNull(jobInstance, "该任务实例不存在");
			jobInfoDetail.setInstance(jobInstance);
			DataProductionClusterEntity cluster = clusterService.getById(jobInstance.getClusterId());
			jobInfoDetail.setCluster(cluster);
			DataProductionTaskHistoryEntity history = historyService.getById(jobInstance.getHistoryId());
			history.setConfig(JSONUtil.parseObject(history.getConfigJson()));
			if (Asserts.isNotNull(history.getClusterConfigurationId())) {
				jobInfoDetail.setClusterConfiguration(
						clusterConfigurationService.getClusterConfigById(history.getClusterConfigurationId()));
			}
			jobInfoDetail.setHistory(history);
			jobInfoDetail.setJobHistory(instanceHistoryService.getJobHistory(id));
			pool.push(key, jobInfoDetail);
		}
		if (!isCoercive && !inRefreshPlan(jobInfoDetail.getInstance())) {
			return jobInfoDetail.getInstance();
		}
		DataProductionTaskInstanceHistoryEntity jobHistoryJson = instanceHistoryService.refreshJobHistory(
				id, jobInfoDetail.getCluster().getJobManagerHost(),
				jobInfoDetail.getInstance().getJid(), jobInfoDetail.isNeedSave());
		DataProductionTaskInstanceHistoryEntity jobHistory = instanceHistoryService.getJobHistoryInfo(jobHistoryJson);
		//设置最新任务执行结果
		jobInfoDetail.setJobHistory(jobHistory);
		JobStatus checkStatus = null;
		if (JobStatus.isDone(jobInfoDetail.getInstance().getStatus())
				&& (Asserts.isNull(jobHistory.getJob()) || jobHistory.isError())) {
			checkStatus = checkJobStatus(jobInfoDetail);
			if (checkStatus.isDone()) {
				jobInfoDetail.getInstance().setStatus(checkStatus.getValue());
				if (jobInfoDetail.isNeedSave()) {
					instanceService.updateById(jobInfoDetail.getInstance());
				}
				pool.refresh(jobInfoDetail);
				return jobInfoDetail.getInstance();
			}
		}
		String status = jobInfoDetail.getInstance().getStatus();
		boolean jobStatusChanged = false;
		if (Asserts.isNull(jobInfoDetail.getJobHistory().getJob()) || jobInfoDetail.getJobHistory().isError()) {
			if (Asserts.isNotNull(checkStatus)) {
				jobInfoDetail.getInstance().setStatus(checkStatus.getValue());
			} else {
				jobInfoDetail.getInstance().setStatus(JobStatus.UNKNOWN.getValue());
			}
		} else {
			jobInfoDetail.getInstance().setDuration(
					jobInfoDetail.getJobHistory().getJob().get(FlinkRestResultConstant.JOB_DURATION).asLong() / 1000);
			String jobState = jobInfoDetail.getJobHistory().getJob().get(FlinkRestResultConstant.JOB_STATE).asText();
			jobInfoDetail.getInstance()
					.setStatus(jobState);
		}
		/*DataProductionTaskInstanceEntity currentInstance = jobInfoDetail.getInstance();
		if (GatewayType.LOCAL.getLongValue().equals(currentInstance.getTaskType()) && JobStatus.UNKNOWN.getValue().equals(jobInfoDetail.getInstance().getStatus())) {
			jobInfoDetail.getInstance().setStatus(jobHistory.isError() ? JobStatus.FAILED.getValue() : JobStatus.FINISHED.getValue());
		}*/
		if (JobStatus.isDone(jobInfoDetail.getInstance().getStatus())
				&& !status.equals(jobInfoDetail.getInstance().getStatus())) {
			jobStatusChanged = true;
			jobInfoDetail.getInstance().setFinishTime(new Date());
			// handleJobDone(jobInfoDetail.getInstance());
		}
		if (isCoercive) {
			DaemonFactory.addTask(DaemonTaskConfig.build(FlinkJobTask.TYPE, jobInfoDetail.getInstance().getId().intValue()));
		}
		if (jobStatusChanged || jobInfoDetail.isNeedSave()) {
			instanceService.updateById(jobInfoDetail.getInstance());
		}
		pool.refresh(jobInfoDetail);
		return jobInfoDetail.getInstance();
	}

	@Override
	public JobStatus checkJobStatus(JobInfoDetail jobInfoDetail) {
		JobConfig jobConfig = new JobConfig();
		if (Asserts.isNull(jobInfoDetail.getClusterConfiguration())) {
			return JobStatus.UNKNOWN;
		}
		Map<String, Object> gatewayConfigMap = clusterConfigurationService
				.getGatewayConfig(jobInfoDetail.getClusterConfiguration().getId());
		jobConfig.buildGatewayConfig(gatewayConfigMap);
		GatewayConfig gatewayConfig = jobConfig.getGatewayConfig();
		gatewayConfig.setType(GatewayType.getByCode(jobInfoDetail.getCluster().getType().toString()));
		gatewayConfig.getClusterConfig().setAppId(jobInfoDetail.getCluster().getName());
		Gateway gateway = Gateway.build(gatewayConfig);
		return gateway.getJobStatusById(jobInfoDetail.getCluster().getName());
	}

	@Override
	public DataProductionTaskEntity getTaskInfoById(Integer id) {
		DataProductionTaskEntity task = this.getById(id);
		if (task != null) {
			task.parseConfig();
			LambdaQueryWrapper<DataProductionTaskStatementEntity> wrapper = new LambdaQueryWrapper<>();
			wrapper.eq(DataProductionTaskStatementEntity::getTaskId, id).last(" limit 1");
			DataProductionTaskStatementEntity statement = statementDao.selectOne(wrapper);
			if (task.getClusterId() != null) {
				DataProductionClusterEntity cluster = clusterService.getById(task.getClusterId());
				if (cluster != null) {
					task.setClusterName(cluster.getName());
				}
			}
			if (statement != null) {
				task.setStatement(statement.getStatement());
			}
			DataProductionTaskInstanceEntity jobInstance = instanceService.getJobInstanceByTaskId(id);
			if (Asserts.isNotNull(jobInstance) && !JobStatus.isDone(jobInstance.getStatus())) {
				task.setJobInstanceId(jobInstance.getId());
			} else {
				task.setJobInstanceId(0L);
			}
		}
		return task;
	}

	@Override
	public void handleJobDone(DataProductionTaskInstanceEntity jobInstance) {
		if (Asserts.isNull(jobInstance.getTaskId())) {
			return;
		}
		//DataProductionTaskEntity task = getTaskInfoById(jobInstance.getTaskId());
		DataProductionTaskEntity updateTask = getById(jobInstance.getTaskId());
		updateTask.setId(jobInstance.getTaskId().longValue());
		updateTask.setJobInstanceId(null);
		if (!JobLifeCycle.ONLINE.equalsValue(jobInstance.getStep())) {
			updateById(updateTask);
			return;
		}
		Integer jobInstanceId = jobInstance.getId().intValue();
		// 获取任务历史信息
		DataProductionTaskInstanceHistoryEntity jobHistory = instanceHistoryService.getJobHistory(jobInstanceId);
		// 获取任务历史信息的jobJson
		String jobJson = jobHistory.getJobJson();
		ObjectNode jsonNodes = JSONUtil.parseObject(jobJson);
		if (jsonNodes.has(FlinkRestResultConstant.ERRORS)) {
			return;
		}
		/*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long asLongStartTime = jsonNodes.get("start-time").asLong(); // 获取任务历史信息的start-time
		long asLongEndTime = jsonNodes.get("end-time").asLong(); // 获取任务历史信息的end-time

		if (asLongEndTime < asLongStartTime) {
			asLongEndTime = System.currentTimeMillis();
		}
		String startTime = dateFormat.format(asLongStartTime);
		String endTime = dateFormat.format(asLongEndTime);
		// Long duration = jsonNodes.get("duration").asLong();
		String duration = getDuration(asLongStartTime, asLongEndTime);
		// 获取任务的 duration 使用的是 start-time 和 end-time 计算
		// 不采用 duration 字段
		String clusterJson = jobHistory.getClusterJson(); // 获取任务历史信息的clusterJson 主要获取 jobManagerHost
		ObjectNode clusterJsonNodes = JSONUtil.parseObject(clusterJson);*/

		/*String jobManagerHost = clusterJsonNodes.get("jobManagerHost").asText();*/

		// TODO 报警后续做
		/*if (Asserts.isNotNull(task.getAlertGroupId())) {
			AlertGroup alertGroup = alertGroupService.getAlertGroupInfo(task.getAlertGroupId());
			if (Asserts.isNotNull(alertGroup)) {
				AlertMsg alertMsg = new AlertMsg();
				alertMsg.setAlertType("Flink 实时监控");
				alertMsg.setAlertTime(dateFormat.format(new Date()));
				alertMsg.setJobID(jobInstance.getJid());
				alertMsg.setJobName(task.getName());
				alertMsg.setJobType(task.getDialect());
				alertMsg.setJobStatus(jobInstance.getStatus());
				alertMsg.setJobStartTime(startTime);
				alertMsg.setJobEndTime(endTime);
				alertMsg.setJobDuration(duration);

				String linkUrl = "http://" + jobManagerHost + "/#/job/" + jobInstance.getJid() + "/overview";
				String exceptionUrl = "http://" + jobManagerHost + "/#/job/" + jobInstance.getJid() + "/exceptions";

				for (AlertInstance alertInstance : alertGroup.getInstances()) {
					if (alertInstance == null
							|| (Asserts.isNotNull(alertInstance.getEnabled()) && !alertInstance.getEnabled())) {
						continue;
					}
					Map<String, String> map = JSONUtil.toMap(alertInstance.getParams());
					if (map.get("msgtype").equals(ShowType.MARKDOWN.getValue())) {
						alertMsg.setLinkUrl("[跳转至该任务的 FlinkWeb](" + linkUrl + ")");
						alertMsg.setExceptionUrl("[点击查看该任务的异常日志](" + exceptionUrl + ")");
					} else {
						alertMsg.setLinkUrl(linkUrl);
						alertMsg.setExceptionUrl(exceptionUrl);
					}
					sendAlert(alertInstance, jobInstance, task, alertMsg);
				}
			}
		}*/
		updateTask.setStep(JobLifeCycle.RELEASE.getValue());
		updateById(updateTask);
	}

	@Override
	public JobResult submitTaskCommon(StudioExecuteDto studioExecuteDto) {
		Integer taskId = studioExecuteDto.getTaskId();
		DataProductionTaskEntity task = this.getTaskInfoById(taskId);
		if (Dialect.notFlinkSql(Dialect.getByCode(task.getDialect().toString()).getValue())) {
			return executeCommonSql(SqlDto.build(taskId, task.getStatement(), task.getSqlDbType(),
					task.getDatabaseId() == null ? null : task.getDatabaseId().intValue(), task.getOpenTrans(), task.getPvdataNum()));
		}
		ProcessEntity process = ProcessContextHolder.registerProcess(
				ProcessEntity.init(ProcessType.FLINKSUBMIT, studioExecuteDto.getAccessToken()));
		process.setProjectId(task.getProjectId());
		process.info("Initializing Flink job config...");
		JobConfig config = buildJobConfig(task);
		process.info("Initializing Flink job config end");
		// init UDF TODO
		/*udfService.init(task.getStatement(), config);*/

		//TODO
		/*if (GatewayType.KUBERNETES_APPLICATION.equalsValue(config.getType())) {
			loadDocker(id, config.getClusterConfigurationId(), config.getGatewayConfig());
		}*/
		JobManager jobManager = JobManager.build(config);
		process.start();
		if (!config.isJarTask()) {
			JobResult jobResult = jobManager.executeSql(task.getStatement());
			process.finish("Submit Flink SQL succeed.");
			process.infoEnd();
			ProcessContextHolder.clear();
			return jobResult;
		} else {
			JobResult jobResult = jobManager.executeJar();
			process.finish("Submit Flink Jar succeed.");
			process.infoEnd();
			ProcessContextHolder.clear();
			return jobResult;
		}
	}

	@Override
	public JobResult submitTask(Long taskId) {
		DataProductionTaskEntity task = this.getTaskInfoById(taskId.intValue());
		StudioExecuteDto studioExecuteDto = new StudioExecuteDto();
		studioExecuteDto.setTaskId(taskId.intValue());
		studioExecuteDto.setAccessToken(getAccessToken());
		return FlinkApiFactory.getByVersion(FlinkVersion.getByValue(task.getFlinkVersion())).submitTask(studioExecuteDto);
	}

	@Override
	public JobResult scheduleTaskCommon(DataProductionScheduleNodeRecordEntity nodeRecord) {
		JobResult jobResult = new JobResult();
		try {
			Integer taskId = nodeRecord.getTaskId();
			DataProductionTaskEntity task = this.getTaskInfoById(taskId);
			if (Dialect.notFlinkSql(Dialect.getByCode(task.getDialect().toString()).getValue())) {
				return executeCommonSql(SqlDto.build(taskId, task.getStatement(), task.getSqlDbType(),
						task.getDatabaseId() == null ? null : task.getDatabaseId().intValue(), task.getOpenTrans(), task.getPvdataNum(), nodeRecord));
			}
			String processId = nodeRecord.getId() + DataProductionScheduleNodeRecordEntity.SCHEDULE_NODE_RECORD;
			ProcessEntity process = ProcessContextHolder.registerProcess(
					ProcessEntity.init(ProcessType.FLINKEXECUTE, processId));
			process.setNodeRecordId(nodeRecord.getId().intValue());
			process.setProjectId(task.getProjectId());
			process.info("Initializing Flink job config...");
			JobConfig config = buildJobConfig(task);
			process.info("Initializing Flink job config end");
			// init UDF TODO
			/*udfService.init(task.getStatement(), config);*/

			//TODO
		/*if (GatewayType.KUBERNETES_APPLICATION.equalsValue(config.getType())) {
			loadDocker(id, config.getClusterConfigurationId(), config.getGatewayConfig());
		}*/
			JobManager jobManager = JobManager.build(config);
			process.start();
			if (!config.isJarTask()) {
				jobResult = jobManager.executeSql(task.getStatement());
				//process.finish("Submit Flink SQL succeed.");
				jobResult.setLog(ConsolePool.getInstance().get(processId).toString());
				ConsolePool.getInstance().remove(processId);
				ProcessPool.getInstance().remove(processId);
				//process.infoEnd();
				ProcessContextHolder.clear();
				return jobResult;
			} else {
				jobResult = jobManager.executeJar();
				//process.finish("Submit Flink Jar succeed.");
				jobResult.setLog(ConsolePool.getInstance().get(processId).toString());
				ConsolePool.getInstance().remove(processId);
				ProcessPool.getInstance().remove(processId);
				//process.infoEnd();
				ProcessContextHolder.clear();
				return jobResult;
			}
		} catch (Exception e) {
			jobResult.setLog(LogUtil.getError(e));
			return jobResult;
		}
	}

	@Override
	public JobResult scheduleTask(DataProductionScheduleNodeRecordEntity nodeRecord) {
		DataProductionTaskEntity task = getTaskInfoById(nodeRecord.getTaskId());
		return FlinkApiFactory.getByVersion(FlinkVersion.getByValue(task.getFlinkVersion())).scheduleTask(nodeRecord);
	}


	@Override
	public ConsoleLog getConsoleLog() {
		ConsoleLog consoleLog = new ConsoleLog();
		String accessToken = getAccessToken();
		if (ConsolePool.getInstance().exist(accessToken)) {
			String msg = ConsolePool.getInstance().get(accessToken).toString();
			consoleLog.setLog(msg);
			consoleLog.setEnd(msg.endsWith(ProcessEntity.INFO_END));
			return consoleLog;
		} else {
			return consoleLog;
		}
	}

	@Override
	public void clearLog() {
		//清空之前的日志
		String accessToken = getAccessToken();
		ConsolePool.getInstance().remove(accessToken);
		ProcessPool.getInstance().remove(accessToken);
	}

	@Override
	public void endConsoleLog() {
		ProcessEntity process = ProcessPool.getInstance().get(getAccessToken());
		if (process == null) {
			return;
		}
		process.infoEnd();
	}

	@SneakyThrows
	@Override
	public SelectResult getJobDataByJobId(String jobId) {
		LambdaQueryWrapper<DataProductionTaskHistoryEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataProductionTaskHistoryEntity::getJobId, jobId).last(" limit 1");
		DataProductionTaskHistoryEntity one = historyService.getOne(wrapper);
		if (StringUtil.isBlank(one.getResult())) {
			return SelectResult.buildSuccess(jobId);
		}
		return SingletonObject.OBJECT_MAPPER.readValue(one.getResult(), SelectResult.class);
	}

	@Override
	public void updateInfoByCatalogue(DataProductionCatalogueEntity entity) {
		baseMapper.updateInfoByCatalogue(entity);
	}

	@Override
	public void delHistory(List<Long> idList) {
		historyService.delete(idList);
	}

	@Override
	public PageResult<DataProductionTaskHistoryVO> pageHistory(DataProductionTaskHistoryQuery query) {
		return historyService.page(query);
	}

	@Override
	public String getInstanceError(Integer historyId) {
		return instanceHistoryService.getInstanceError(historyId);
	}

	@Override
	public DataProductionTaskInstanceHistoryVO getHistoryClusterInfo(Integer historyId) {
		return instanceHistoryService.getByHistoryId(historyId);
	}

	@Override
	public void clearLogWithOutKey() {
		String accessToken = getAccessToken();
		if (ConsolePool.getInstance().exist(accessToken)) {
			ConsolePool.getInstance().push(accessToken, new StringBuilder());
		}
	}

	@Override
	public void savepoint(Integer historyId, String type) {
		if (Asserts.isNullString(type)) {
			type = SavePointType.CANCEL.getValue();
		}
		savepointTask(historyId, type);
	}

	@Override
	public boolean savepointTask(Integer historyId, String savePointType) {
		LambdaQueryWrapper<DataProductionTaskInstanceEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataProductionTaskInstanceEntity::getHistoryId, historyId).last("limit 1");
		DataProductionTaskInstanceEntity instance = instanceService.getOne(wrapper);
		if (JobStatus.isDone(instance.getStatus())) {
			throw new ServerException("canceljob".equals(savePointType) ? "该作业生命周期已经结束" : "该作业生命周期已经结束，无需 savepoint");
		}
		DataProductionTaskEntity taskEntity = baseMapper.selectById(instance.getTaskId());
		SavepointDto savepointDto = new SavepointDto(instance.getId().intValue(), historyId, savePointType);
		return FlinkApiFactory.getByVersion(FlinkVersion.getByValue(taskEntity.getFlinkVersion())).savepointJobInstance(savepointDto);
		//return savepointJobInstance(instance.getId().intValue(), historyId, savePointType);
	}

	@Override
	public PageResult<DataProductionTaskSavepointsVO> pageSavepoint(DataProductionTaskSavepointsQuery query) {
		return savepointsService.page(query);
	}

	@Override
	public void delSavepoint(List<Long> idList) {
		savepointsService.delete(idList);
	}

	@Override
	public List<DataProductionTaskVO> listEnv() {
		LambdaQueryWrapper<DataProductionTaskEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataProductionTaskEntity::getProjectId, getProjectId())
				.eq(DataProductionTaskEntity::getDialect, Dialect.FLINKSQLENV.getCode()).eq(DataProductionTaskEntity::getEnabled, true)
				.orderByDesc(DataProductionTaskEntity::getCreateTime).orderByDesc(DataProductionTaskEntity::getId);
		return DataProductionTaskConvert.INSTANCE.convertList(list(wrapper));
	}

	@Override
	public boolean savepointJobInstance(Integer jobInstanceId, Integer historyId, String savePointType) {
		ProcessEntity process = ProcessContextHolder.registerProcess(
				ProcessEntity.init(ProcessType.SAVEPOINT, ""));
		DataProductionTaskInstanceEntity jobInstance = instanceService.getById(jobInstanceId);
		if (Asserts.isNull(jobInstance)) {
			throw new ServerException("没有查询到对应的flink任务实例，请检查数据完整性");
		}
		process.setProjectId(jobInstance.getProjectId().longValue());
		DataProductionClusterEntity cluster = clusterService.getById(jobInstance.getClusterId());
		Asserts.checkNotNull(cluster, "flink任务实例对应的集群不存在，请检查数据完整性");
		String jobId = jobInstance.getJid();
		boolean useGateway = false;
		JobConfig jobConfig = new JobConfig();
		jobConfig.setType(GatewayType.getByCode(cluster.getType().toString()).getLongValue());
		DataProductionTaskEntity task = getTaskInfoById(jobInstance.getTaskId());

		if (Asserts.isNotNull(cluster.getClusterConfigurationId())) {
			Map<String, Object> gatewayConfig = clusterConfigurationService
					.getGatewayConfig(cluster.getClusterConfigurationId().longValue());
			// 如果是k8s application 模式,且不是sql任务，则需要补齐statement 内的自定义配置
			if (Dialect.KUBERNETES_APPLICATION.equalsVal(Dialect.get(task.getDialect().toString()).getValue())) {
				LambdaQueryWrapper<DataProductionTaskStatementEntity> wrapper = new LambdaQueryWrapper<>();
				wrapper.eq(DataProductionTaskStatementEntity::getTaskId, jobInstance.getTaskId()).last(" limit 1");
				DataProductionTaskStatementEntity statement = statementDao.selectOne(wrapper);
				Map<String, Object> statementConfig = JSONUtil.toMap(statement.getStatement(), String.class,
						Object.class);
				gatewayConfig.putAll(statementConfig);
			}
			jobConfig.buildGatewayConfig(gatewayConfig);
			jobConfig.getGatewayConfig().getClusterConfig().setAppId(cluster.getName());
			useGateway = true;
		}
		jobConfig.setTaskId(jobInstance.getTaskId());
		jobConfig.setAddress(cluster.getJobManagerHost());
		JobManager jobManager = JobManager.build(jobConfig);
		jobManager.setUseGateway(useGateway);
		if ("canceljob".equals(savePointType)) {
			return jobManager.cancel(jobId);
		}
		SavePointResult savePointResult = jobManager.savepoint(jobId, savePointType, null);
		if (Asserts.isNotNull(savePointResult.getJobInfos())) {
			for (JobInfo item : savePointResult.getJobInfos()) {
				if (Asserts.isEqualsIgnoreCase(jobId, item.getJobId()) && Asserts.isNotNull(jobConfig.getTaskId())) {
					DataProductionTaskSavepointsEntity savepoints = new DataProductionTaskSavepointsEntity();
					savepoints.setProjectId(jobInstance.getProjectId());
					savepoints.setName(savePointType);
					savepoints.setType(savePointType);
					savepoints.setPath(item.getSavePoint());
					savepoints.setTaskId(jobConfig.getTaskId());
					savepoints.setHistoryId(historyId);
					savepointsService.save(savepoints);
				}
			}
			return true;
		}
		return false;
	}

	private JobConfig buildJobConfig(DataProductionTaskEntity task) {
		ProcessEntity process = ProcessContextHolder.getProcess();
		try {
			boolean isJarTask = Dialect.FLINKJAR.equalsVal(Dialect.getByCode(task.getDialect().toString()).getValue())
					|| Dialect.KUBERNETES_APPLICATION.equalsVal(Dialect.getByCode(task.getDialect().toString()).getValue());
		/*if (!isJarTask && Asserts.isNotNull(task.getFragment()) ? task.getFragment() : false) {
			String flinkWithSql = dataBaseService.getEnabledFlinkWithSql();
			if (Asserts.isNotNullString(flinkWithSql)) {
				task.setStatement(flinkWithSql + "\r\n" + task.getStatement());
			}
		}*/
			if (!isJarTask && Asserts.isNotNull(task.getEnvId())) {
				DataProductionTaskEntity envTask = getTaskInfoById(task.getEnvId().intValue());
				if (Asserts.isNotNull(envTask) && Asserts.isNotNullString(envTask.getStatement())) {
					task.setStatement(envTask.getStatement() + "\r\n" + task.getStatement());
				}
			}
			JobConfig config = task.buildSubmitConfig();
			config.setJarTask(isJarTask);
			if (!JobManager.useGateway(config.getType())) {
				config.setAddress(clusterService.buildEnvironmentAddress(config.isUseRemote(), task.getClusterId() == null ? null : task.getClusterId().intValue()));
				if (isJarTask) {
					if (!config.isUseRemote()) {
						config.getConfig().put(RestOptions.PORT.key(), "8081");
						config.getConfig().put(RestOptions.BIND_PORT.key(), "8081-9999");
					}
					DataProductionJarEntity jar = jarService.getById(task.getJarId());
					config.setJobManagePort(config.isUseRemote() ? clusterService.buildJobManagePort(config.getAddress()) : FlinkConstant.JOB_MANAGE_PORT);
					config.setJarPath(jar.getPath());
					config.setEntryPointClassName(jar.getMainClass());
					config.setJarParams(jar.getParams());
				}
			}
			// support custom K8s app submit, rather than clusterConfiguration TODO
			else if (Dialect.KUBERNETES_APPLICATION.equalsVal(Dialect.getByCode(task.getDialect().toString()).getValue())
					&& GatewayType.KUBERNETES_APPLICATION.equalsValue(config.getType())) {
				Map<String, Object> taskConfig = JSONUtil.toMap(task.getStatement(), String.class, Object.class);
				Map<String, Object> clusterConfiguration = clusterConfigurationService
						.getGatewayConfig(task.getClusterConfigurationId());
				clusterConfiguration.putAll((Map<String, Object>) taskConfig.get("appConfig"));
				clusterConfiguration.put("taskCustomConfig", taskConfig);
				config.buildGatewayConfig(clusterConfiguration);
			} else {
				Map<String, Object> gatewayConfig = clusterConfigurationService
						.getGatewayConfig(task.getClusterConfigurationId());
				// submit application type with clusterConfiguration
				if (GatewayType.YARN_APPLICATION.equalsValue(config.getType())
						|| GatewayType.KUBERNETES_APPLICATION.equalsValue(config.getType())) {
					if (!isJarTask) {
						SystemConfiguration systemConfiguration = ProjectSystemConfiguration.getByProjectId(task.getProjectId());
						gatewayConfig.put("userJarPath", systemConfiguration.getSqlSubmitJarPath());
						gatewayConfig.put("userJarParas",
								systemConfiguration.getSqlSubmitJarParas() + buildParas(config.getTaskId()));
						gatewayConfig.put("userJarMainAppClass", systemConfiguration.getSqlSubmitJarMainAppClass());
					} else {
						DataProductionJarEntity jar = jarService.getById(task.getJarId());
						gatewayConfig.put("userJarPath", jar.getPath());
						gatewayConfig.put("userJarParas", jar.getParams());
						gatewayConfig.put("userJarMainAppClass", jar.getMainClass());
					}
				}
				config.buildGatewayConfig(gatewayConfig);
				config.addGatewayConfig(task.parseConfig());
			}
			Long taskId = task.getId();
			//设置保存点
			setSavepoint(config, taskId);
			//TODO
			//config.setVariables(fragmentVariableService.listEnabledVariables());
			return config;
		} catch (Exception e) {
			process.error(LogUtil.getError(e));
			process.infoEnd();
			ProcessContextHolder.clear();
			throw new ServerException(LogUtil.getError(e));
		}

	}

	private void setSavepoint(JobConfig config, Long taskId) {
		ProcessEntity process = ProcessContextHolder.getProcess();
		process.info("Initializing Flink job savepoint");
		switch (config.getSavePointStrategy()) {
			case LATEST:
				process.info("Set lastest savepoint...");
				DataProductionTaskSavepointsEntity latestSavepoints = savepointsService.getLatestSavepointByTaskId(taskId);
				if (Asserts.isNotNull(latestSavepoints)) {
					config.setSavePointPath(latestSavepoints.getPath());
					config.getConfig().put("execution.savepoint.path", latestSavepoints.getPath());
					process.info("Set lastest savepoint success:" + latestSavepoints.getPath());
				} else {
					process.info("Not found lastest savepoint,set savepoint failed");
				}
				break;
			case EARLIEST:
				process.info("Set earliest savepoint...");
				DataProductionTaskSavepointsEntity earliestSavepoints = savepointsService.getEarliestSavepointByTaskId(taskId);
				if (Asserts.isNotNull(earliestSavepoints)) {
					config.setSavePointPath(earliestSavepoints.getPath());
					config.getConfig().put("execution.savepoint.path", earliestSavepoints.getPath());
					process.info("Set earliest savepoint success:" + earliestSavepoints.getPath());
				} else {
					process.info("Not found earliest savepoint,set savepoint failed");
				}
				break;
			case CUSTOM:
				process.info("Set custom savepoint...");
				config.setSavePointPath(config.getSavePointPath());
				config.getConfig().put("execution.savepoint.path", config.getSavePointPath());
				process.info("Set custom savepoint success:" + config.getSavePointPath());
				break;
			default:
				config.setSavePointPath(null);
		}
		process.info("Initializing Flink job savepoint end");
	}


	private boolean inRefreshPlan(DataProductionTaskInstanceEntity jobInstance) {
		return (!JobStatus.isDone(jobInstance.getStatus())) || (Asserts.isNotNull(jobInstance.getFinishTime())
				&& Duration.between(DateUtils.asLocalDateTime(jobInstance.getFinishTime()), LocalDateTime.now()).toMinutes() < 1);
	}

	@Override
	public JobResult executeFlinkSqlCommon(StudioExecuteDto studioExecuteDto) {
		ProcessEntity process = ProcessContextHolder.registerProcess(
				ProcessEntity.init(ProcessType.FLINKEXECUTE, studioExecuteDto.getAccessToken()));
		process.setProjectId(studioExecuteDto.getProjectId());
		addFlinkSQLEnv(studioExecuteDto);
		process.info("Initializing Flink job config...");
		JobConfig config = studioExecuteDto.getJobConfig();
		process.info("Initializing Flink job config end");
		if (GatewayType.LOCAL.getLongValue().equals(studioExecuteDto.getType())) {
			config.getConfig().put(RestOptions.PORT.key(), "8081");
			config.getConfig().put(RestOptions.BIND_PORT.key(), "8081-9999");
		}
		buildSession(config);
		//设置保存点
		setSavepoint(config, studioExecuteDto.getTaskId().longValue());
		// init UDF TODO
		//udfService.init(studioExecuteDTO.getStatement(), config);
		JobManager jobManager = JobManager.build(config);
		process.start();
		JobResult jobResult = jobManager.executeSql(studioExecuteDto.getStatement());
		process.finish("Execute Flink SQL succeed.");
		RunTimeUtil.recovery(jobManager);
		process.infoEnd();
		ProcessContextHolder.clear();
		return jobResult;
	}

	private JobResult executeFlinkSql(StudioExecuteDto studioExecuteDto) {
		studioExecuteDto.setAccessToken(getAccessToken());
		studioExecuteDto.setProjectId(getProjectId());
		return FlinkApiFactory.getByVersion(FlinkVersion.getByValue(studioExecuteDto.getFlinkVersion())).executeFlinkSql(studioExecuteDto);
	}

	@SneakyThrows
	private JobResult executeCommonSql(SqlDto sqlDto) {
		String processId = sqlDto.getNodeRecord() != null ? sqlDto.getNodeRecord().getId() + DataProductionScheduleNodeRecordEntity.SCHEDULE_NODE_RECORD : getAccessToken();
		ProcessEntity process = ProcessContextHolder.registerProcess(
				ProcessEntity.init(ProcessType.SQLEXECUTE, processId));
		JobResult result = new JobResult();
		result.setStatement(sqlDto.getStatement());
		result.setStartTimeNow();
		process.info("Initializing database connection...");
		DataProductionTaskVO task = get(sqlDto.getTaskId().longValue());
		//添加历史
		DataProductionTaskHistoryEntity history = new DataProductionTaskHistoryEntity();
		history.setOrgId(task.getOrgId());
		history.setProjectId(task.getProjectId().intValue());
		history.setTaskId(sqlDto.getTaskId());
		history.setExecuteNo(UUID.randomUUID().toString().replaceAll("-", ""));
		history.setJobName(task.getName());
		history.setStatus(Job.JobStatus.RUNNING.ordinal());
		history.setDialect(Dialect.SQL.getCode());
		history.setStatement(sqlDto.getStatement());
		history.setCreator(task.getCreator());
		SqlConfigJson sqlConfigJson = new SqlConfigJson();
		sqlConfigJson.setSqlDbType(sqlDto.getSqlDbType());
		sqlConfigJson.setPvdataNum(sqlDto.getMaxRowNum());
		boolean ifMiddleDb = SqlDbType.MIDDLE_DB.getValue().equals(sqlDto.getSqlDbType());
		DataDatabaseDto database;
		if (ifMiddleDb) {
			history.setSqlDbType(SqlDbType.MIDDLE_DB.getValue());
			DataProjectCacheBean project = getProject(task.getProjectId());
			database = new DataDatabaseDto();
			database.setName(project.getName() + "<中台库>");
			database.setDatabaseName(project.getDbName());
			database.setDatabaseSchema(project.getDbSchema());
			database.setJdbcUrl(project.getDbUrl());
			database.setUserName(project.getDbUsername());
			database.setPassword(project.getDbPassword());
			database.setDatabaseType(project.getDbType());
		} else {
			history.setSqlDbType(SqlDbType.DATABASE.getValue());
			history.setDatabaseId(sqlDto.getDatabaseId());
			database = DataSourceApi.getById(sqlDto.getDatabaseId().longValue()).getData();
		}
		sqlConfigJson.setDatabase(database);
		history.setConfigJson(SingletonObject.OBJECT_MAPPER.writeValueAsString(sqlConfigJson));
		history.setStartTime(new Date());
		if (sqlDto.getNodeRecord() != null) {
			history.setExecuteType(sqlDto.getNodeRecord().getExecuteType());
			history.setScheduleId(sqlDto.getNodeRecord().getTaskScheduleId());
			history.setScheduleNodeId(sqlDto.getNodeRecord().getScheduleNodeId());
			history.setScheduleRecordId(sqlDto.getNodeRecord().getScheduleRecordId());
			history.setScheduleNodeRecordId(sqlDto.getNodeRecord().getId().intValue());
		}
		historyService.save(history);
		try {
			// zrx
			ProductTypeEnum productTypeEnum = ProductTypeEnum.getByIndex(database.getDatabaseType());
			IMetaDataByJdbcService metaDataService = new MetaDataByJdbcServiceImpl(productTypeEnum);
			process.start();
			String jdbcUrl = database.getJdbcUrl();
			String userName = database.getUserName();
			String password = database.getPassword();
			JdbcSelectResult selectResult = metaDataService.queryDataBySql(jdbcUrl, productTypeEnum.name(), userName, password, sqlDto.getStatement(), sqlDto.getOpenTrans(), sqlDto.getMaxRowNum());
			process.finish("Execute sql succeed.");
			result.setResult(selectResult);
			result.setSuccess(selectResult.getSuccess());
			if (sqlDto.getNodeRecord() != null) {
				result.setLog(ConsolePool.getInstance().get(processId).toString());
				ConsolePool.getInstance().remove(processId);
				ProcessPool.getInstance().remove(processId);
			} else {
				process.infoEnd();
			}
			result.setEndTimeNow();
			//更新历史
			history.setEndTime(new Date());
			history.setStatus(result.isSuccess() ? Job.JobStatus.SUCCESS.ordinal() : Job.JobStatus.FAILED.ordinal());
			history.setError(selectResult.getErrorMsg());
			history.setResult(SingletonObject.OBJECT_MAPPER.writeValueAsString(selectResult));
			historyService.updateById(history);
		} catch (Exception e) {
			result.setSuccess(false);
			process.error(LogUtil.getError(e));
			if (sqlDto.getNodeRecord() != null) {
				result.setLog(ConsolePool.getInstance().get(processId).toString());
				ConsolePool.getInstance().remove(processId);
				ProcessPool.getInstance().remove(processId);
			} else {
				process.infoEnd();
			}
			result.setEndTimeNow();
			//历史结束
			history.setEndTime(new Date());
			history.setStatus(Job.JobStatus.FAILED.ordinal());
			history.setError(LogUtil.getError(e));
			historyService.updateById(history);
		} finally {
			ProcessContextHolder.clear();
		}
		return result;
	}

	@Override
	public List<SqlExplainResult> explainFlinkSqlCommon(StudioExecuteDto studioExecuteDto) {
		buildExecuteDto(studioExecuteDto);
		ProcessEntity process = ProcessContextHolder.registerProcess(
				ProcessEntity.init(ProcessType.FLINKEXPLAIN, studioExecuteDto.getAccessToken()));
		process.setProjectId(studioExecuteDto.getProjectId());
		addFlinkSQLEnv(studioExecuteDto);
		process.info("Initializing Flink job config...");
		JobConfig config = studioExecuteDto.getJobConfig();
		process.info("Initializing Flink job config end");
		// If you are using explainSql | getStreamGraph | getJobPlan, make the dialect change to local.
		config.buildLocal();
		buildSession(config);
		// init UDF TODO 后续添加UDF支持
		//udfService.init(studioExecuteDTO.getStatement(), config);
		process.info("build JobManager...");
		JobManager jobManager = JobManager.build(config);
		process.info("build JobManager end");
		process.start();
		List<SqlExplainResult> sqlExplainResults;
		try {
			sqlExplainResults = jobManager.explainSql(studioExecuteDto.getStatement())
					.getSqlExplainResults();
		} catch (Exception e) {
			process.error(LogUtil.getError(e));
			process.infoEnd();
			throw new RuntimeException(LogUtil.getError(e));
		}
		process.finish();
		if (studioExecuteDto.getProcessEnd()) {
			process.infoEnd();
		}
		ProcessContextHolder.clear();
		return sqlExplainResults;
	}

	private List<SqlExplainResult> explainFlinkSql(StudioExecuteDto studioExecuteDto) {
		studioExecuteDto.setAccessToken(getAccessToken());
		studioExecuteDto.setProjectId(getProjectId());
		return FlinkApiFactory.getByVersion(FlinkVersion.getByValue(studioExecuteDto.getFlinkVersion())).explainFlinkSql(studioExecuteDto);
	}

	private void buildExecuteDto(StudioExecuteDto studioExecuteDto) {
		studioExecuteDto.setType(GatewayType.getByCode(studioExecuteDto.getType()).getLongValue());
		String dialect = Dialect.getByCode(studioExecuteDto.getDialect()).getValue();
		studioExecuteDto.setDialect(dialect);
		studioExecuteDto.setTaskId(studioExecuteDto.getId());
	}

	private List<SqlExplainResult> explainCommonSql(StudioExecuteDto studioExecuteDTO) {
		ProcessEntity process = ProcessContextHolder.registerProcess(
				ProcessEntity.init(ProcessType.SQLEXPLAIN, getAccessToken()));
		process.info("Initializing database connection...");
		//调用dataBase的Api
		boolean ifMiddleDb = SqlDbType.MIDDLE_DB.getValue().equals(studioExecuteDTO.getSqlDbType());
		DataDatabaseDto dataBase = ifMiddleDb ? null : DataSourceApi.getById(studioExecuteDTO.getDatabaseId().longValue()).getData();
		// zrx
		ProductTypeEnum productTypeEnum = ProductTypeEnum.getByIndex(ifMiddleDb ? getProject().getDbType() : dataBase.getDatabaseType());
		IMetaDataByJdbcService metaDataService = new MetaDataByJdbcServiceImpl(productTypeEnum);
		process.start();
		List<SqlExplainResult> explain = metaDataService.explain(studioExecuteDTO.getStatement(), productTypeEnum.name());
		process.finish();
		if (studioExecuteDTO.getProcessEnd()) {
			process.infoEnd();
		}
		return explain;
	}

	/**
	 * 如果选择了flinksql执行环境，则需要添加 TODO
	 *
	 * @param statementDTO
	 */
	private void addFlinkSQLEnv(AbstractStatementDto statementDTO) {
		ProcessEntity process = ProcessContextHolder.getProcess();
		process.info("Start initialize FlinkSQLEnv:");
		if (statementDTO.isFragment()) {
			process.config("Variable opened.");

			// initialize global variables
			process.info("Initializing global variables...");
			//statementDTO.setVariables(fragmentVariableService.listEnabledVariables());
			process.infoSuccess();

			// initialize database variables
			process.info("Initializing database variables...");
			//String flinkWithSql = dataBaseService.getEnabledFlinkWithSql();
			/*if (Asserts.isNotNullString(flinkWithSql)) {
				statementDTO.setStatement(flinkWithSql + "\n" + statementDTO.getStatement());
				process.infoSuccess();
			} else {
				process.info("No variables are loaded.");
			}*/
			process.info("No variables are loaded.");
		}

		// initialize flinksql environment, such as flink catalog
		if (Asserts.isNotNull(statementDTO.getEnvId()) && !statementDTO.getEnvId().equals(0)) {
			process.config("FlinkSQLEnv opened.");
			process.info("Initializing FlinkSQLEnv...");
			DataProductionTaskVO task = get(statementDTO.getEnvId().longValue());
			if (Asserts.isNotNull(task) && Asserts.isNotNullString(task.getStatement())) {
				statementDTO.setStatement(task.getStatement() + "\n" + statementDTO.getStatement());
				process.infoSuccess();
			} else {
				process.info("No FlinkSQLEnv are loaded.");
			}
		}
		process.info("Finish initialize FlinkSQLEnv.");
	}

	private void buildSession(JobConfig config) {
		ProcessEntity process = ProcessContextHolder.getProcess();
		try {
			// If you are using a shared session, configure the current jobManager address
			if (!config.isUseSession()) {
				config.setAddress(clusterService.buildEnvironmentAddress(config.isUseRemote(), config.getClusterId()));
			}
		} catch (Exception e) {
			process.error(LogUtil.getError(e));
			process.infoEnd();
			ProcessContextHolder.clear();
			throw new RuntimeException(e.getMessage());
		}

	}
}
