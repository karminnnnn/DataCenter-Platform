package net.srt.service;

import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataGovernanceQualityTaskColumnVO;
import net.srt.query.DataGovernanceQualityTaskColumnQuery;
import net.srt.entity.DataGovernanceQualityTaskColumnEntity;

import java.util.List;

/**
 * 数据治理-字段检测记录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-06-23
 */
public interface DataGovernanceQualityTaskColumnService extends BaseService<DataGovernanceQualityTaskColumnEntity> {

    PageResult<DataGovernanceQualityTaskColumnVO> page(DataGovernanceQualityTaskColumnQuery query);

    void save(DataGovernanceQualityTaskColumnVO vo);

    void update(DataGovernanceQualityTaskColumnVO vo);

    void delete(List<Long> idList);
}
