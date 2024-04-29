package net.srt.convert;

import net.srt.api.module.data.development.dto.DataProductionTaskDto;
import net.srt.entity.DataProductionTaskEntity;
import net.srt.vo.DataProductionTaskVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据生产任务
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2022-12-05
*/
@Mapper
public interface DataProductionTaskConvert {
    DataProductionTaskConvert INSTANCE = Mappers.getMapper(DataProductionTaskConvert.class);

    DataProductionTaskEntity convert(DataProductionTaskVO vo);

	DataProductionTaskDto convertDto(DataProductionTaskEntity entity);

    DataProductionTaskVO convert(DataProductionTaskEntity entity);

    List<DataProductionTaskVO> convertList(List<DataProductionTaskEntity> list);

}
