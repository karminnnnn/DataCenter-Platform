package net.srt.convert;

import net.srt.api.module.data.governance.dto.DataGovernanceMasterColumnDto;
import net.srt.entity.DataGovernanceMasterColumnEntity;
import net.srt.vo.DataGovernanceMasterColumnVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据治理-主数据模型字段
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-09-27
*/
@Mapper
public interface DataGovernanceMasterColumnConvert {
    DataGovernanceMasterColumnConvert INSTANCE = Mappers.getMapper(DataGovernanceMasterColumnConvert.class);

    DataGovernanceMasterColumnEntity convert(DataGovernanceMasterColumnVO vo);

    DataGovernanceMasterColumnVO convert(DataGovernanceMasterColumnEntity entity);

    List<DataGovernanceMasterColumnVO> convertList(List<DataGovernanceMasterColumnEntity> list);

	List<DataGovernanceMasterColumnDto> convertDtoList(List<DataGovernanceMasterColumnEntity> list);
}
