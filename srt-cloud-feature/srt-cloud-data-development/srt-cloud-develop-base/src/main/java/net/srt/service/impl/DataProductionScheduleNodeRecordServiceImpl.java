package net.srt.service.impl;

import lombok.AllArgsConstructor;
import net.srt.convert.DataProductionScheduleNodeRecordConvert;
import net.srt.dao.DataProductionScheduleNodeRecordDao;
import net.srt.entity.DataProductionScheduleNodeRecordEntity;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.service.DataProductionScheduleNodeRecordService;
import net.srt.vo.DataProductionScheduleNodeRecordVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据生产-作业调度节点记录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-01-16
 */
@Service
@AllArgsConstructor
public class DataProductionScheduleNodeRecordServiceImpl extends BaseServiceImpl<DataProductionScheduleNodeRecordDao, DataProductionScheduleNodeRecordEntity> implements DataProductionScheduleNodeRecordService {


    @Override
    public void save(DataProductionScheduleNodeRecordVO vo) {
        DataProductionScheduleNodeRecordEntity entity = DataProductionScheduleNodeRecordConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(DataProductionScheduleNodeRecordVO vo) {
        DataProductionScheduleNodeRecordEntity entity = DataProductionScheduleNodeRecordConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}
