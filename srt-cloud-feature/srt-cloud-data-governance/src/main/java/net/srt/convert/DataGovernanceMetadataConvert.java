package net.srt.convert;

import net.srt.api.module.data.governance.dto.DataGovernanceMetadataDto;
import net.srt.entity.DataGovernanceMetadataEntity;
import net.srt.vo.DataGovernanceMetadataVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 数据治理-元数据
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-03-29
 */
@Mapper
public interface DataGovernanceMetadataConvert {
	DataGovernanceMetadataConvert INSTANCE = Mappers.getMapper(DataGovernanceMetadataConvert.class);

	DataGovernanceMetadataEntity convert(DataGovernanceMetadataVO vo);

	DataGovernanceMetadataEntity convert(DataGovernanceMetadataDto dto);

	DataGovernanceMetadataVO convert(DataGovernanceMetadataEntity entity);

	DataGovernanceMetadataDto convertDto(DataGovernanceMetadataEntity entity);

	List<DataGovernanceMetadataVO> convertList(List<DataGovernanceMetadataEntity> list);

	List<DataGovernanceMetadataDto> convertDtoList(List<DataGovernanceMetadataEntity> list);

}
