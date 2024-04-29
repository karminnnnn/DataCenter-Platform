package net.srt.convert;

import net.srt.entity.DataGovernanceStandardCategoryEntity;
import net.srt.vo.DataGovernanceStandardCategoryVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据治理-标准目录
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-05-05
*/
@Mapper
public interface DataGovernanceStandardCategoryConvert {
    DataGovernanceStandardCategoryConvert INSTANCE = Mappers.getMapper(DataGovernanceStandardCategoryConvert.class);

    DataGovernanceStandardCategoryEntity convert(DataGovernanceStandardCategoryVO vo);

    DataGovernanceStandardCategoryVO convert(DataGovernanceStandardCategoryEntity entity);

    List<DataGovernanceStandardCategoryVO> convertList(List<DataGovernanceStandardCategoryEntity> list);

}