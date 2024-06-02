package net.srt.convert;


import net.srt.api.module.data.integrate.dto.DataFieldDto;
import net.srt.entity.DataFieldEntity;
import net.srt.vo.DataFieldVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DataFieldConvert {
    DataFieldConvert INSTANCE = Mappers.getMapper(DataFieldConvert.class);

    DataFieldEntity convert(DataFieldVO vo);

    DataFieldVO convert(DataFieldEntity entity);

    DataFieldDto convertDto(DataFieldEntity entity);

    List<DataFieldVO> convertList(List<DataFieldEntity> list);
}