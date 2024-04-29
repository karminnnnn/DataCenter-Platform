package net.srt.convert;

import net.srt.entity.DataProductionScheduleNodeRecordEntity;
import net.srt.vo.DataProductionScheduleNodeRecordVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据生产-作业调度节点记录
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-01-16
*/
@Mapper
public interface DataProductionScheduleNodeRecordConvert {
    DataProductionScheduleNodeRecordConvert INSTANCE = Mappers.getMapper(DataProductionScheduleNodeRecordConvert.class);

    DataProductionScheduleNodeRecordEntity convert(DataProductionScheduleNodeRecordVO vo);

    DataProductionScheduleNodeRecordVO convert(DataProductionScheduleNodeRecordEntity entity);

    List<DataProductionScheduleNodeRecordVO> convertList(List<DataProductionScheduleNodeRecordEntity> list);

}