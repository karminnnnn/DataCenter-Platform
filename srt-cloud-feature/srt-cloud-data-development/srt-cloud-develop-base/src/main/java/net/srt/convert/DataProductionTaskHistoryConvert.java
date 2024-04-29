package net.srt.convert;

import net.srt.entity.DataProductionTaskHistoryEntity;
import net.srt.vo.DataProductionTaskHistoryVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据生产任务历史
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2022-12-19
*/
@Mapper
public interface DataProductionTaskHistoryConvert {
    DataProductionTaskHistoryConvert INSTANCE = Mappers.getMapper(DataProductionTaskHistoryConvert.class);

    DataProductionTaskHistoryEntity convert(DataProductionTaskHistoryVO vo);

    DataProductionTaskHistoryVO convert(DataProductionTaskHistoryEntity entity);

    List<DataProductionTaskHistoryVO> convertList(List<DataProductionTaskHistoryEntity> list);

}