package net.srt.convert;

import net.srt.api.module.data.governance.dto.DataGovernanceMasterModelDto;
import net.srt.entity.DataGovernanceMasterModelEntity;
import net.srt.framework.common.utils.Result;
import net.srt.vo.DataGovernanceMasterModelVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据治理-主数据模型
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-09-27
*/
@Mapper
public interface DataGovernanceMasterModelConvert {
    DataGovernanceMasterModelConvert INSTANCE = Mappers.getMapper(DataGovernanceMasterModelConvert.class);

    DataGovernanceMasterModelEntity convert(DataGovernanceMasterModelVO vo);

    DataGovernanceMasterModelVO convert(DataGovernanceMasterModelEntity entity);

    List<DataGovernanceMasterModelVO> convertList(List<DataGovernanceMasterModelEntity> list);

	DataGovernanceMasterModelDto convertDto(DataGovernanceMasterModelEntity modelEntity);
}
