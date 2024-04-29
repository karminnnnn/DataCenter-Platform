package net.srt.service;

import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataGovernanceQualityTaskVO;
import net.srt.query.DataGovernanceQualityTaskQuery;
import net.srt.entity.DataGovernanceQualityTaskEntity;

import java.util.List;

/**
 * 数据治理-质量任务
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-06-23
 */
public interface DataGovernanceQualityTaskService extends BaseService<DataGovernanceQualityTaskEntity> {

    PageResult<DataGovernanceQualityTaskVO> page(DataGovernanceQualityTaskQuery query);

    void save(DataGovernanceQualityTaskVO vo);

    void update(DataGovernanceQualityTaskVO vo);

    void delete(List<Long> idList);

	void dealNotFinished();
}
