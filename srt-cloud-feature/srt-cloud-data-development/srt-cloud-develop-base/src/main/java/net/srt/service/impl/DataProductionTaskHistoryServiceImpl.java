package net.srt.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import net.srt.convert.DataProductionTaskHistoryConvert;
import net.srt.dao.DataProductionTaskHistoryDao;
import net.srt.dao.DataProductionTaskInstanceDao;
import net.srt.dao.DataProductionTaskInstanceHistoryDao;
import net.srt.entity.DataProductionTaskHistoryEntity;
import net.srt.flink.common.config.Dialect;
import net.srt.flink.core.result.ResultPool;
import net.srt.flink.core.result.SelectResult;
import net.srt.flink.gateway.GatewayType;
import net.srt.framework.common.constant.Constant;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.DateUtils;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataProductionTaskHistoryQuery;
import net.srt.service.DataProductionTaskHistoryService;
import net.srt.vo.DataProductionTaskHistoryVO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.SingletonObject;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据生产任务历史
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-19
 */
@Service
@AllArgsConstructor
public class DataProductionTaskHistoryServiceImpl extends BaseServiceImpl<DataProductionTaskHistoryDao, DataProductionTaskHistoryEntity> implements DataProductionTaskHistoryService {

	private final DataProductionTaskInstanceDao instanceDao;
	private final DataProductionTaskInstanceHistoryDao instanceHistoryDao;

	@Override
	public PageResult<DataProductionTaskHistoryVO> page(DataProductionTaskHistoryQuery query) {
		query.setType(StringUtil.isBlank(query.getType()) ? null : GatewayType.getByCode(query.getType()).getLongValue());
		// 查询参数
		Map<String, Object> params = getParams(query);
		// 分页查询
		query.setOrder("dpth.id");
		IPage<DataProductionTaskHistoryEntity> page = getPage(query);
		params.put(Constant.PAGE, page);
		// 数据列表
		List<DataProductionTaskHistoryEntity> list = baseMapper.getHistoryList(params);
		for (DataProductionTaskHistoryEntity history : list) {
			history.setDuration(DateUtils.getDuration(history.getStartTime().getTime(), history.getFinishTime() != null ? history.getFinishTime().getTime() :
					Dialect.SQL.getCode().equals(history.getDialect()) ? history.getEndTime() != null ? history.getEndTime().getTime() : System.currentTimeMillis() :
					StringUtil.isNotBlank(history.getJid()) ? System.currentTimeMillis() : history.getEndTime() != null ? history.getEndTime().getTime() : System.currentTimeMillis()));
		}
		return new PageResult<>(DataProductionTaskHistoryConvert.INSTANCE.convertList(list), page.getTotal());
	}

	private Map<String, Object> getParams(DataProductionTaskHistoryQuery query) {
		Map<String, Object> params = new HashMap<>();
		params.put("recordId", query.getRecordId());
		params.put("nodeRecordId", query.getNodeRecordId());
		params.put("taskId", query.getTaskId());
		params.put("jobName", query.getJobName());
		params.put("status", query.getStatus());
		params.put("instanceStatus", query.getInstanceStatus());
		params.put("sqlDbType", query.getSqlDbType());
		params.put("databaseId", query.getDatabaseId());
		params.put("type", query.getType());
		params.put("dialect", query.getDialect());
		params.put("clusterId", query.getClusterId());
		params.put("clusterConfigurationId", query.getClusterConfigurationId());
		params.put("startTime", query.getStartTime());
		params.put("endTime", query.getEndTime());
		params.put("finishTime", query.getFinishTime());
		// 数据权限
		params.put(Constant.DATA_SCOPE, getDataScope("dpth", "dpth", "org_id", "project_id", true, true));

		return params;
	}

	@Override
	public void save(DataProductionTaskHistoryVO vo) {
		DataProductionTaskHistoryEntity entity = DataProductionTaskHistoryConvert.INSTANCE.convert(vo);

		baseMapper.insert(entity);
	}

	@Override
	public void update(DataProductionTaskHistoryVO vo) {
		DataProductionTaskHistoryEntity entity = DataProductionTaskHistoryConvert.INSTANCE.convert(vo);

		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		for (Long id : idList) {
			removeById(id);
			Map<String, Object> delMap = new HashMap<>();
			delMap.put("history_id", id);
			instanceDao.deleteByMap(delMap);
			instanceHistoryDao.deleteByMap(delMap);
		}
	}

	@Override
	@Async("scheduledPoolTaskExecutor")
	public void pullResult(DataProductionTaskHistoryEntity historyEntity) {
		DataProductionTaskHistoryEntity history = baseMapper.selectById(historyEntity.getId());
		if (StringUtil.isBlank(history.getJobId())) {
			return;
		}
		if (ResultPool.realGet(history.getJobId()) == null) {
			return;
		}
		//调试的时候才会查看结果，10分钟肯定够了
		for (int i = 0; i < 300; i++) {
			try {
				Thread.sleep(2000);
			} catch (Exception ignored) {
			}
			//1s拉一次，更新到history的result字段
			SelectResult selectResult = ResultPool.get(history.getJobId());
			try {
				history.setResult(SingletonObject.OBJECT_MAPPER.writeValueAsString(selectResult));
				baseMapper.updateById(history);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			if (selectResult.isEnd()) {
				break;
			}
		}
		ResultPool.remove(history.getJobId());
	}


}
