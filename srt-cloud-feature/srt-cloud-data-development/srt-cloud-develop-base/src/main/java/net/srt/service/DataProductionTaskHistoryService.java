package net.srt.service;

import net.srt.entity.DataProductionTaskHistoryEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.query.DataProductionTaskHistoryQuery;
import net.srt.vo.DataProductionTaskHistoryVO;

import java.util.List;

/**
 * 数据生产任务历史
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-19
 */
public interface DataProductionTaskHistoryService extends BaseService<DataProductionTaskHistoryEntity> {

    PageResult<DataProductionTaskHistoryVO> page(DataProductionTaskHistoryQuery query);

    void save(DataProductionTaskHistoryVO vo);

    void update(DataProductionTaskHistoryVO vo);

    void delete(List<Long> idList);

	void pullResult(DataProductionTaskHistoryEntity historyEntity);
}
