package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.srt.convert.DataProductionTaskInstanceHistoryConvert;
import net.srt.dao.DataProductionTaskInstanceHistoryDao;
import net.srt.entity.DataProductionTaskInstanceHistoryEntity;
import net.srt.flink.common.assertion.Asserts;
import net.srt.flink.common.utils.JSONUtil;
import net.srt.flink.core.api.FlinkAPI;
import net.srt.flink.core.constant.FlinkRestResultConstant;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataProductionTaskInstanceHistoryQuery;
import net.srt.service.DataProductionTaskInstanceHistoryService;
import net.srt.vo.DataProductionTaskInstanceHistoryVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 数据生产任务实例历史
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-20
 */
@Slf4j
@Service
@AllArgsConstructor
public class DataProductionTaskInstanceHistoryServiceImpl extends BaseServiceImpl<DataProductionTaskInstanceHistoryDao, DataProductionTaskInstanceHistoryEntity> implements DataProductionTaskInstanceHistoryService {

	@Override
	public PageResult<DataProductionTaskInstanceHistoryVO> page(DataProductionTaskInstanceHistoryQuery query) {
		IPage<DataProductionTaskInstanceHistoryEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataProductionTaskInstanceHistoryConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	private LambdaQueryWrapper<DataProductionTaskInstanceHistoryEntity> getWrapper(DataProductionTaskInstanceHistoryQuery query) {
		LambdaQueryWrapper<DataProductionTaskInstanceHistoryEntity> wrapper = Wrappers.lambdaQuery();

		return wrapper;
	}

	@Override
	public void save(DataProductionTaskInstanceHistoryVO vo) {
		DataProductionTaskInstanceHistoryEntity entity = DataProductionTaskInstanceHistoryConvert.INSTANCE.convert(vo);

		baseMapper.insert(entity);
	}

	@Override
	public void update(DataProductionTaskInstanceHistoryVO vo) {
		DataProductionTaskInstanceHistoryEntity entity = DataProductionTaskInstanceHistoryConvert.INSTANCE.convert(vo);

		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		removeByIds(idList);
	}

	@Override
	public DataProductionTaskInstanceHistoryEntity getJobHistory(Integer id) {
		return getJobHistoryInfo(baseMapper.selectById(id));
	}

	@Override
	public DataProductionTaskInstanceHistoryEntity refreshJobHistory(Integer id, String jobManagerHost, String jobId, boolean needSave) {
		DataProductionTaskInstanceHistoryEntity jobHistory = new DataProductionTaskInstanceHistoryEntity();
		jobHistory.setId(id.longValue());
		try {
			//记录最后一条flinksql的相关信息
			JsonNode jobInfo = FlinkAPI.build(jobManagerHost).getJobInfo(jobId);
			if (Asserts.isNull(jobInfo) || jobInfo.has(FlinkRestResultConstant.ERRORS)) {
				final DataProductionTaskInstanceHistoryEntity dbHistory = getById(id);
				if (Objects.nonNull(dbHistory)) {
					jobHistory = dbHistory;
				}
				jobHistory.setError(true);
				return jobHistory;
			}
			JsonNode exception = FlinkAPI.build(jobManagerHost).getException(jobId);
			JsonNode checkPoints = FlinkAPI.build(jobManagerHost).getCheckPoints(jobId);
			JsonNode checkPointsConfig = FlinkAPI.build(jobManagerHost).getCheckPointsConfig(jobId);
			JsonNode jobsConfig = FlinkAPI.build(jobManagerHost).getJobsConfig(jobId);
			jobHistory.setJobJson(JSONUtil.toJsonString(jobInfo));
			jobHistory.setExceptionsJson(JSONUtil.toJsonString(exception));
			jobHistory.setCheckpointsJson(JSONUtil.toJsonString(checkPoints));
			jobHistory.setCheckpointsConfigJson(JSONUtil.toJsonString(checkPointsConfig));
			jobHistory.setConfigJson(JSONUtil.toJsonString(jobsConfig));

			//zrx 获取所有flinksql的错误日志，若有一条执行失败，则状态为失败
			/*LinkedHashMap<String, String> sqlJidMap = JSONUtil.parseObject(sqlJid, new TypeReference<LinkedHashMap<String, String>>() {
			});
			if (sqlJidMap != null) {
				//获取每个sql对应的jobId的错误信息，帮助用户定位问题
				List<FlinkJobMsg> exceptionList = new ArrayList<>();
				boolean hasError = false;
				for (Map.Entry<String, String> entry : sqlJidMap.entrySet()) {

					String sql = entry.getKey();
					String jid = entry.getValue();

					JsonNode theJob = FlinkAPI.build(jobManagerHost).getJobInfo(jid);
					if (Asserts.isNull(theJob) || theJob.has(FlinkRestResultConstant.ERRORS)) {
						jobHistory.setError(true);
						hasError = true;
					}
					JsonNode errorMsg = FlinkAPI.build(jobManagerHost).getException(jid);
					FlinkJobMsg exceptionMsg = new FlinkJobMsg();
					exceptionMsg.setSql(sql);
					exceptionMsg.setJid(jid);
					if (errorMsg == null) {
						ObjectNode objectNode = JSONUtil.createObjectNode();
						if (Asserts.isNull(theJob)) {
							objectNode.put(FlinkRestResultConstant.ROOT_EXCEPTION, "获取任务信息失败，请自行排查！（Local 模式下由于 miniCluster 会自动关闭可能导致无法及时获取实例状态，导致失败，可不予理会）");
						} else if (theJob.has(FlinkRestResultConstant.ERRORS)) {
							objectNode.put(FlinkRestResultConstant.ROOT_EXCEPTION, "获取任务信息出错！\n" + theJob.get(FlinkRestResultConstant.ERRORS).asText());
						} else {
							objectNode.put(FlinkRestResultConstant.ROOT_EXCEPTION, "没有获取到错误日志，请自行排查（Local 模式下由于 miniCluster 会自动关闭可能导致无法及时获取实例状态，导致失败，可不予理会）");
						}
						errorMsg = objectNode;
					} else {
						if (errorMsg.has(FlinkRestResultConstant.ROOT_EXCEPTION) && !"null".equals(errorMsg.get(FlinkRestResultConstant.ROOT_EXCEPTION).asText())) {
							hasError = true;
							jobHistory.setError(true);
							jobHistory.setExecuteError(true);
						}
					}
					exceptionMsg.setJson(errorMsg);
					exceptionList.add(exceptionMsg);
				}
				if (hasError) {
					//有任务出错了，其他任务也没必要执行了，取消即可
					for (String jidVal : sqlJidMap.values()) {
						FlinkAPI.build(jobManagerHost).stop(jidVal);
					}
				}
				jobHistory.setExecuteExceptionsJson(JSONUtil.toJsonString(exceptionList));
			}*/

			if (needSave) {
				updateById(jobHistory);
				/*
				 * if (Asserts.isNotNull(getById(id))) { updateById(jobHistory); } else { save(jobHistory); }
				 */
			}
		} catch (Exception e) {
			log.error("Get flink job info failed !! historyId is {}, jobManagerHost is :{}, jobId is :{}",
					id, jobManagerHost, jobId);
			e.printStackTrace();
		}
		return jobHistory;
	}

	@Override
	public DataProductionTaskInstanceHistoryEntity getJobHistoryInfo(DataProductionTaskInstanceHistoryEntity jobHistory) {
		if (Asserts.isNotNull(jobHistory)) {
			if (Asserts.isNotNullString(jobHistory.getJobJson())) {
				jobHistory.setJob(JSONUtil.parseObject(jobHistory.getJobJson()));
				jobHistory.setJobJson(null);
			}
			if (Asserts.isNotNullString(jobHistory.getExceptionsJson())) {
				jobHistory.setExceptions(JSONUtil.parseObject(jobHistory.getExceptionsJson()));
				jobHistory.setExceptionsJson(null);
			}
			if (Asserts.isNotNullString(jobHistory.getCheckpointsJson())) {
				jobHistory.setCheckpoints(JSONUtil.parseObject(jobHistory.getCheckpointsJson()));
				jobHistory.setCheckpointsJson(null);
			}
			if (Asserts.isNotNullString(jobHistory.getCheckpointsConfigJson())) {
				jobHistory.setCheckpointsConfig(JSONUtil.parseObject(jobHistory.getCheckpointsConfigJson()));
				jobHistory.setCheckpointsConfigJson(null);
			}
			if (Asserts.isNotNullString(jobHistory.getConfigJson())) {
				jobHistory.setConfig(JSONUtil.parseObject(jobHistory.getConfigJson()));
				jobHistory.setConfigJson(null);
			}
			if (Asserts.isNotNullString(jobHistory.getJarJson())) {
				jobHistory.setJar(JSONUtil.parseObject(jobHistory.getJarJson()));
				jobHistory.setJarJson(null);
			}
			if (Asserts.isNotNullString(jobHistory.getClusterJson())) {
				jobHistory.setCluster(JSONUtil.parseObject(jobHistory.getClusterJson()));
				jobHistory.setClusterJson(null);
			}
			if (Asserts.isNotNullString(jobHistory.getClusterConfigurationJson())) {
				jobHistory.setClusterConfiguration(JSONUtil.parseObject(jobHistory.getClusterConfigurationJson()));
				jobHistory.setClusterConfigurationJson(null);
			}
		}
		return jobHistory;
	}

	@Override
	public String getInstanceError(Integer historyId) {
		LambdaQueryWrapper<DataProductionTaskInstanceHistoryEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataProductionTaskInstanceHistoryEntity::getHistoryId, historyId).last(" limit 1");
		DataProductionTaskInstanceHistoryEntity one = baseMapper.selectOne(wrapper);
		if (one != null) {
			return one.getExceptionsJson();
		}
		return null;
	}

	@Override
	public DataProductionTaskInstanceHistoryVO getByHistoryId(Integer historyId) {
		LambdaQueryWrapper<DataProductionTaskInstanceHistoryEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataProductionTaskInstanceHistoryEntity::getHistoryId, historyId).last(" limit 1");
		DataProductionTaskInstanceHistoryEntity one = baseMapper.selectOne(wrapper);
		return DataProductionTaskInstanceHistoryConvert.INSTANCE.convert(one);
	}

}
