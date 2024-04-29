package net.srt.convert;

import net.srt.api.module.data.governance.dto.DataGovernanceMetadataPropertyDto;
import net.srt.entity.DataGovernanceMetadataPropertyEntity;
import net.srt.vo.DataGovernanceMetadataPropertyVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据治理-元数据属性值
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-03-29
*/
@Mapper
public interface DataGovernanceMetadataPropertyConvert {
    DataGovernanceMetadataPropertyConvert INSTANCE = Mappers.getMapper(DataGovernanceMetadataPropertyConvert.class);

    DataGovernanceMetadataPropertyEntity convert(DataGovernanceMetadataPropertyVO vo);

	DataGovernanceMetadataPropertyEntity convert(DataGovernanceMetadataPropertyDto dto);

    DataGovernanceMetadataPropertyVO convert(DataGovernanceMetadataPropertyEntity entity);

	DataGovernanceMetadataPropertyDto convertDto(DataGovernanceMetadataPropertyEntity entity);

    List<DataGovernanceMetadataPropertyVO> convertList(List<DataGovernanceMetadataPropertyEntity> list);

}
