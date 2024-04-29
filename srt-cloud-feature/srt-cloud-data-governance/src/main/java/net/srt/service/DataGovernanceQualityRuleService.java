package net.srt.service;

import net.srt.entity.DataGovernanceQualityRuleEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.query.DataGovernanceQualityRuleQuery;
import net.srt.vo.DataGovernanceQualityRuleVO;

import java.util.List;

/**
 * 数据治理-质量规则
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-28
 */
public interface DataGovernanceQualityRuleService extends BaseService<DataGovernanceQualityRuleEntity> {

    PageResult<DataGovernanceQualityRuleVO> page(DataGovernanceQualityRuleQuery query);

    void save(DataGovernanceQualityRuleVO vo);

    void update(DataGovernanceQualityRuleVO vo);

    void delete(List<Long> idList);
}
