package net.srt.convert;

import net.srt.api.module.data.integrate.dto.DataDatabaseDto;
import net.srt.entity.DataDatabaseEntity;
import net.srt.vo.DataDatabaseVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DataDatabaseConvert {
    DataDatabaseConvert INSTANCE = Mappers.getMapper(DataDatabaseConvert.class);

    @Mapping(source = "datasourceId", target = "datasourceId")
    DataDatabaseEntity convert(DataDatabaseVO vo);

    @Mapping(source = "datasourceId", target = "datasourceId")
    DataDatabaseVO convert(DataDatabaseEntity entity);

    @Mapping(source = "datasourceId", target = "datasourceId")
    DataDatabaseDto convertDto(DataDatabaseEntity entity);

    @Mapping(source = "datasourceId", target = "datasourceId")
    List<DataDatabaseVO> convertList(List<DataDatabaseEntity> list);
}
