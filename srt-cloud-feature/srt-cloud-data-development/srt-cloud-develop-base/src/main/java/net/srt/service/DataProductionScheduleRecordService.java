package net.srt.service;

import net.srt.entity.DataProductionScheduleRecordEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.query.DataProductionScheduleRecordQuery;
import net.srt.vo.DataProductionScheduleRecordVO;

import java.util.List;

/**
 * 数据生产-作业调度记录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-01-16
 */
public interface DataProductionScheduleRecordService extends BaseService<DataProductionScheduleRecordEntity> {

    PageResult<DataProductionScheduleRecordVO> page(DataProductionScheduleRecordQuery query);

    void save(DataProductionScheduleRecordVO vo);

    void update(DataProductionScheduleRecordVO vo);

    void delete(List<Long> idList);

}
