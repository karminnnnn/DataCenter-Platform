package net.srt.convert;

import net.srt.api.module.data.governance.dto.DataGovernanceMetadataCollectRecordDto;
import net.srt.entity.DataGovernanceMetadataCollectRecordEntity;
import net.srt.vo.DataGovernanceMetadataCollectRecordVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据治理-元数据采集任务记录
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-04-04
*/
@Mapper
public interface DataGovernanceMetadataCollectRecordConvert {
    DataGovernanceMetadataCollectRecordConvert INSTANCE = Mappers.getMapper(DataGovernanceMetadataCollectRecordConvert.class);

    DataGovernanceMetadataCollectRecordEntity convert(DataGovernanceMetadataCollectRecordVO vo);

	DataGovernanceMetadataCollectRecordEntity convert(DataGovernanceMetadataCollectRecordDto dto);

    DataGovernanceMetadataCollectRecordVO convert(DataGovernanceMetadataCollectRecordEntity entity);

    List<DataGovernanceMetadataCollectRecordVO> convertList(List<DataGovernanceMetadataCollectRecordEntity> list);

}
