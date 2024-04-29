package net.srt.convert;

import net.srt.entity.DataGovernanceLabelModelEntity;
import net.srt.vo.DataGovernanceLabelModelVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据治理-标签实体
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-12-24
*/
@Mapper
public interface DataGovernanceLabelModelConvert {
    DataGovernanceLabelModelConvert INSTANCE = Mappers.getMapper(DataGovernanceLabelModelConvert.class);

    DataGovernanceLabelModelEntity convert(DataGovernanceLabelModelVO vo);

    DataGovernanceLabelModelVO convert(DataGovernanceLabelModelEntity entity);

    List<DataGovernanceLabelModelVO> convertList(List<DataGovernanceLabelModelEntity> list);

}
