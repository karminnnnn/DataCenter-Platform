package net.srt.convert;

import net.srt.entity.DataGovernanceMetadataStandardRelEntity;
import net.srt.vo.DataGovernanceMetadataStandardRelVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据治理-元数据标准关联表
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-05-23
*/
@Mapper
public interface DataGovernanceMetadataStandardRelConvert {
    DataGovernanceMetadataStandardRelConvert INSTANCE = Mappers.getMapper(DataGovernanceMetadataStandardRelConvert.class);

    DataGovernanceMetadataStandardRelEntity convert(DataGovernanceMetadataStandardRelVO vo);

    DataGovernanceMetadataStandardRelVO convert(DataGovernanceMetadataStandardRelEntity entity);

    List<DataGovernanceMetadataStandardRelVO> convertList(List<DataGovernanceMetadataStandardRelEntity> list);

}