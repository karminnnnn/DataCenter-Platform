package net.srt.convert;

import net.srt.entity.DataProductionScheduleRecordEntity;
import net.srt.vo.DataProductionScheduleRecordVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据生产-作业调度记录
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-01-16
*/
@Mapper
public interface DataProductionScheduleRecordConvert {
    DataProductionScheduleRecordConvert INSTANCE = Mappers.getMapper(DataProductionScheduleRecordConvert.class);

    DataProductionScheduleRecordEntity convert(DataProductionScheduleRecordVO vo);

    DataProductionScheduleRecordVO convert(DataProductionScheduleRecordEntity entity);

    List<DataProductionScheduleRecordVO> convertList(List<DataProductionScheduleRecordEntity> list);

}