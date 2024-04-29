package net.srt.convert;

import net.srt.api.module.data.governance.dto.DataGovernanceMetadataCollectDto;
import net.srt.entity.DataGovernanceMetadataCollectEntity;
import net.srt.vo.DataGovernanceMetadataCollectVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 数据治理-元数据采集
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-04-01
 */
@Mapper
public interface DataGovernanceMetadataCollectConvert {
	DataGovernanceMetadataCollectConvert INSTANCE = Mappers.getMapper(DataGovernanceMetadataCollectConvert.class);

	DataGovernanceMetadataCollectEntity convert(DataGovernanceMetadataCollectVO vo);

	DataGovernanceMetadataCollectEntity convert(DataGovernanceMetadataCollectDto dto);

	DataGovernanceMetadataCollectDto convertDto(DataGovernanceMetadataCollectEntity entity);

	DataGovernanceMetadataCollectVO convert(DataGovernanceMetadataCollectEntity entity);

	List<DataGovernanceMetadataCollectVO> convertList(List<DataGovernanceMetadataCollectEntity> list);

}
