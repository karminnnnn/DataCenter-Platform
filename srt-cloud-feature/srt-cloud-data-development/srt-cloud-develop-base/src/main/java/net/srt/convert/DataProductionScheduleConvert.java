package net.srt.convert;

import net.srt.api.module.data.development.dto.DataProductionScheduleDto;
import net.srt.entity.DataProductionScheduleEntity;
import net.srt.vo.DataProductionScheduleVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据生产-作业调度
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-01-12
*/
@Mapper
public interface DataProductionScheduleConvert {
    DataProductionScheduleConvert INSTANCE = Mappers.getMapper(DataProductionScheduleConvert.class);

    DataProductionScheduleEntity convert(DataProductionScheduleVO vo);

	DataProductionScheduleDto convertDto(DataProductionScheduleEntity entity);

    DataProductionScheduleVO convert(DataProductionScheduleEntity entity);

    List<DataProductionScheduleVO> convertList(List<DataProductionScheduleEntity> list);

}
