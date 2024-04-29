package net.srt.convert;

import net.srt.entity.DataProductionTaskSavepointsEntity;
import net.srt.vo.DataProductionTaskSavepointsVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据生产-作业保存点
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-01-08
*/
@Mapper
public interface DataProductionTaskSavepointsConvert {
    DataProductionTaskSavepointsConvert INSTANCE = Mappers.getMapper(DataProductionTaskSavepointsConvert.class);

    DataProductionTaskSavepointsEntity convert(DataProductionTaskSavepointsVO vo);

    DataProductionTaskSavepointsVO convert(DataProductionTaskSavepointsEntity entity);

    List<DataProductionTaskSavepointsVO> convertList(List<DataProductionTaskSavepointsEntity> list);

}