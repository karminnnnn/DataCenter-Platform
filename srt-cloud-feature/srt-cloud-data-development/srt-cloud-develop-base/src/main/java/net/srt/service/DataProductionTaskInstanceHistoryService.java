package net.srt.service;

import net.srt.entity.DataProductionTaskInstanceHistoryEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.query.DataProductionTaskInstanceHistoryQuery;
import net.srt.vo.DataProductionTaskInstanceHistoryVO;

import java.util.List;

/**
 * 数据生产任务实例历史
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-20
 */
public interface DataProductionTaskInstanceHistoryService extends BaseService<DataProductionTaskInstanceHistoryEntity> {

	PageResult<DataProductionTaskInstanceHistoryVO> page(DataProductionTaskInstanceHistoryQuery query);

	void save(DataProductionTaskInstanceHistoryVO vo);

	void update(DataProductionTaskInstanceHistoryVO vo);

	void delete(List<Long> idList);

	DataProductionTaskInstanceHistoryEntity getJobHistory(Integer id);

	DataProductionTaskInstanceHistoryEntity refreshJobHistory(Integer id, String jobManagerHost, String jid, boolean needSave);

	DataProductionTaskInstanceHistoryEntity getJobHistoryInfo(DataProductionTaskInstanceHistoryEntity jobHistoryJson);

	String getInstanceError(Integer historyId);

	DataProductionTaskInstanceHistoryVO getByHistoryId(Integer historyId);
}
