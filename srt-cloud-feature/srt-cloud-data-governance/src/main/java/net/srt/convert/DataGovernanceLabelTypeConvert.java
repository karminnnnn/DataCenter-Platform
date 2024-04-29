package net.srt.convert;

import net.srt.entity.DataGovernanceLabelTypeEntity;
import net.srt.vo.DataGovernanceLabelTypeVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据治理-标签类型
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-12-24
*/
@Mapper
public interface DataGovernanceLabelTypeConvert {
    DataGovernanceLabelTypeConvert INSTANCE = Mappers.getMapper(DataGovernanceLabelTypeConvert.class);

    DataGovernanceLabelTypeEntity convert(DataGovernanceLabelTypeVO vo);

    DataGovernanceLabelTypeVO convert(DataGovernanceLabelTypeEntity entity);

    List<DataGovernanceLabelTypeVO> convertList(List<DataGovernanceLabelTypeEntity> list);

}