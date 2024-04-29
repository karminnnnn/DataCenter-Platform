package net.srt.service;

import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataGovernanceStandardCodeVO;
import net.srt.query.DataGovernanceStandardCodeQuery;
import net.srt.entity.DataGovernanceStandardCodeEntity;

import java.util.List;

/**
 * 数据治理-标准码表数据
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-19
 */
public interface DataGovernanceStandardCodeService extends BaseService<DataGovernanceStandardCodeEntity> {

    PageResult<DataGovernanceStandardCodeVO> page(DataGovernanceStandardCodeQuery query);

    void save(DataGovernanceStandardCodeVO vo);

    void update(DataGovernanceStandardCodeVO vo);

    void delete(List<Long> idList);
}
