package net.srt.convert;

import net.srt.entity.DataGovernanceMetamodelEntity;
import net.srt.vo.DataGovernanceMetamodelVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据治理-元模型
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-03-28
*/
@Mapper
public interface DataGovernanceMetamodelConvert {
    DataGovernanceMetamodelConvert INSTANCE = Mappers.getMapper(DataGovernanceMetamodelConvert.class);

    DataGovernanceMetamodelEntity convert(DataGovernanceMetamodelVO vo);

    DataGovernanceMetamodelVO convert(DataGovernanceMetamodelEntity entity);

    List<DataGovernanceMetamodelVO> convertList(List<DataGovernanceMetamodelEntity> list);

}