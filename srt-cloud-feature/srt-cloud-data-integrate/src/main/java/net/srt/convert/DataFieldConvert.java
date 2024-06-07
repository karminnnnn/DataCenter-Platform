package net.srt.convert;


import net.srt.api.module.data.integrate.dto.DataFieldDto;
import net.srt.api.module.data.integrate.dto.DataTableDto;
import net.srt.entity.DataFieldEntity;
import net.srt.entity.DataTableEntity;
import net.srt.vo.DataFieldVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DataFieldConvert {
    DataFieldConvert INSTANCE = Mappers.getMapper(DataFieldConvert.class);


    @Mapping(source = "fieldId", target = "id")
    @Mapping(source = "fieldName", target = "fieldName")
    @Mapping(source = "remarks", target = "remarks")
    @Mapping(source = "fieldTypeName", target = "fieldTypeName")
    @Mapping(source = "displaySize", target = "displaySize")
    @Mapping(source = "scaleSize", target = "scaleSize")
    @Mapping(source = "defaultValue", target = "defaultValue")
    DataFieldEntity convert(DataFieldVO vo);

    @Mapping(source = "fieldId", target = "id")
    @Mapping(source = "fieldName", target = "fieldName")
    @Mapping(source = "remarks", target = "remarks")
    @Mapping(source = "fieldTypeName", target = "fieldTypeName")
    @Mapping(source = "displaySize", target = "displaySize")
    @Mapping(source = "scaleSize", target = "scaleSize")
    @Mapping(source = "defaultValue", target = "defaultValue")
    DataFieldEntity convertByDto(DataFieldDto dto);

    @Mapping(source = "id", target = "fieldId")
    @Mapping(source = "fieldName", target = "fieldName")
    @Mapping(source = "remarks", target = "remarks")
    @Mapping(source = "fieldTypeName", target = "fieldTypeName")
    @Mapping(source = "displaySize", target = "displaySize")
    @Mapping(source = "scaleSize", target = "scaleSize")
    @Mapping(source = "defaultValue", target = "defaultValue")
    DataFieldVO convert(DataFieldEntity entity);

    @Mapping(source = "id", target = "fieldId")
    @Mapping(source = "fieldName", target = "fieldName")
    @Mapping(source = "remarks", target = "remarks")
    @Mapping(source = "fieldTypeName", target = "fieldTypeName")
    @Mapping(source = "displaySize", target = "displaySize")
    @Mapping(source = "scaleSize", target = "scaleSize")
    @Mapping(source = "defaultValue", target = "defaultValue")
    DataFieldDto convertDto(DataFieldEntity entity);

    @Mapping(source = "id", target = "fieldId")
    @Mapping(source = "fieldName", target = "fieldName")
    @Mapping(source = "remarks", target = "remarks")
    @Mapping(source = "fieldTypeName", target = "fieldTypeName")
    @Mapping(source = "displaySize", target = "displaySize")
    @Mapping(source = "scaleSize", target = "scaleSize")
    @Mapping(source = "defaultValue", target = "defaultValue")
    List<DataFieldVO> convertList(List<DataFieldEntity> list);
}