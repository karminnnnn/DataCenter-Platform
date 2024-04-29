package net.srt.service;

import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataGovernanceQualityTaskTableVO;
import net.srt.query.DataGovernanceQualityTaskTableQuery;
import net.srt.entity.DataGovernanceQualityTaskTableEntity;

import java.util.List;

/**
 * 数据治理-表检测记录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-06-23
 */
public interface DataGovernanceQualityTaskTableService extends BaseService<DataGovernanceQualityTaskTableEntity> {

    PageResult<DataGovernanceQualityTaskTableVO> page(DataGovernanceQualityTaskTableQuery query);

    void save(DataGovernanceQualityTaskTableVO vo);

    void update(DataGovernanceQualityTaskTableVO vo);

    void delete(List<Long> idList);
}
