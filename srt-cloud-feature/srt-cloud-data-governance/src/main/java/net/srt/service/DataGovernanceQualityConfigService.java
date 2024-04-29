package net.srt.service;

import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataGovernanceQualityConfigVO;
import net.srt.query.DataGovernanceQualityConfigQuery;
import net.srt.entity.DataGovernanceQualityConfigEntity;

import java.util.List;

/**
 * 数据治理-质量规则配置
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-29
 */
public interface DataGovernanceQualityConfigService extends BaseService<DataGovernanceQualityConfigEntity> {

    PageResult<DataGovernanceQualityConfigVO> page(DataGovernanceQualityConfigQuery query);

	DataGovernanceQualityConfigVO get(Long id);

    void save(DataGovernanceQualityConfigVO vo);

    void update(DataGovernanceQualityConfigVO vo);

    void delete(List<Long> idList);

	void online(Long id);

	void offline(Long id);

	void handRun(Long id);
}
