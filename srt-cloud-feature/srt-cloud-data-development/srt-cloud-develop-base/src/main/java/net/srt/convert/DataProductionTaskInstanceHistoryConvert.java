package net.srt.convert;

import net.srt.entity.DataProductionTaskInstanceHistoryEntity;
import net.srt.vo.DataProductionTaskInstanceHistoryVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据生产任务实例历史
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2022-12-20
*/
@Mapper
public interface DataProductionTaskInstanceHistoryConvert {
    DataProductionTaskInstanceHistoryConvert INSTANCE = Mappers.getMapper(DataProductionTaskInstanceHistoryConvert.class);

    DataProductionTaskInstanceHistoryEntity convert(DataProductionTaskInstanceHistoryVO vo);

    DataProductionTaskInstanceHistoryVO convert(DataProductionTaskInstanceHistoryEntity entity);

    List<DataProductionTaskInstanceHistoryVO> convertList(List<DataProductionTaskInstanceHistoryEntity> list);

}