package net.srt.service;

import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataGovernanceMasterDistributeLogVO;
import net.srt.query.DataGovernanceMasterDistributeLogQuery;
import net.srt.entity.DataGovernanceMasterDistributeLogEntity;

import java.util.List;

/**
 * 数据治理-主数据派发日志
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-10-08
 */
public interface DataGovernanceMasterDistributeLogService extends BaseService<DataGovernanceMasterDistributeLogEntity> {

    PageResult<DataGovernanceMasterDistributeLogVO> page(DataGovernanceMasterDistributeLogQuery query);

    void save(DataGovernanceMasterDistributeLogVO vo);

    void update(DataGovernanceMasterDistributeLogVO vo);

    void delete(List<Long> idList);
}
