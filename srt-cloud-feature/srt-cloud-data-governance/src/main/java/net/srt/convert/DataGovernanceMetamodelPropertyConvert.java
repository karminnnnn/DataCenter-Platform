package net.srt.convert;

import net.srt.entity.DataGovernanceMetamodelPropertyEntity;
import net.srt.vo.DataGovernanceMetamodelPropertyVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据治理-元模型属性
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-03-28
*/
@Mapper
public interface DataGovernanceMetamodelPropertyConvert {
    DataGovernanceMetamodelPropertyConvert INSTANCE = Mappers.getMapper(DataGovernanceMetamodelPropertyConvert.class);

    DataGovernanceMetamodelPropertyEntity convert(DataGovernanceMetamodelPropertyVO vo);

    DataGovernanceMetamodelPropertyVO convert(DataGovernanceMetamodelPropertyEntity entity);

    List<DataGovernanceMetamodelPropertyVO> convertList(List<DataGovernanceMetamodelPropertyEntity> list);

}