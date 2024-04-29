package net.srt.convert;

import net.srt.entity.DataGovernanceQualityConfigCategoryEntity;
import net.srt.vo.DataGovernanceQualityConfigCategoryVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据治理-规则配置目录
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-05-28
*/
@Mapper
public interface DataGovernanceQualityConfigCategoryConvert {
    DataGovernanceQualityConfigCategoryConvert INSTANCE = Mappers.getMapper(DataGovernanceQualityConfigCategoryConvert.class);

    DataGovernanceQualityConfigCategoryEntity convert(DataGovernanceQualityConfigCategoryVO vo);

    DataGovernanceQualityConfigCategoryVO convert(DataGovernanceQualityConfigCategoryEntity entity);

    List<DataGovernanceQualityConfigCategoryVO> convertList(List<DataGovernanceQualityConfigCategoryEntity> list);

}