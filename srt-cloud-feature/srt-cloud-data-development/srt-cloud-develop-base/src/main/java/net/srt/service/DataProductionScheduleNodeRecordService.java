package net.srt.service;

import net.srt.entity.DataProductionScheduleNodeRecordEntity;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataProductionScheduleNodeRecordVO;

import java.util.List;

/**
 * 数据生产-作业调度节点记录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-01-16
 */
public interface DataProductionScheduleNodeRecordService extends BaseService<DataProductionScheduleNodeRecordEntity> {

    void save(DataProductionScheduleNodeRecordVO vo);

    void update(DataProductionScheduleNodeRecordVO vo);

    void delete(List<Long> idList);
}
