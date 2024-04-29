package net.srt.service;

import net.srt.dto.StandardCheckDto;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataGovernanceStandardVO;
import net.srt.query.DataGovernanceStandardQuery;
import net.srt.entity.DataGovernanceStandardEntity;

import java.util.List;

/**
 * 数据治理-数据标准
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-05
 */
public interface DataGovernanceStandardService extends BaseService<DataGovernanceStandardEntity> {

    PageResult<DataGovernanceStandardVO> page(DataGovernanceStandardQuery query);

    void save(DataGovernanceStandardVO vo);

    void update(DataGovernanceStandardVO vo);

    void delete(List<Long> idList);

	void online(Long id);

	void offline(Long id);

	List<DataGovernanceStandardVO> listTableCode();

	StandardCheckDto checkStandard(Long metadataId, Long standardId);
}
