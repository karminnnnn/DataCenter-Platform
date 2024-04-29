package net.srt.convert;

import net.srt.entity.DataGovernanceQualityRuleEntity;
import net.srt.vo.DataGovernanceQualityRuleVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据治理-质量规则
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-05-28
*/
@Mapper
public interface DataGovernanceQualityRuleConvert {
    DataGovernanceQualityRuleConvert INSTANCE = Mappers.getMapper(DataGovernanceQualityRuleConvert.class);

    DataGovernanceQualityRuleEntity convert(DataGovernanceQualityRuleVO vo);

    DataGovernanceQualityRuleVO convert(DataGovernanceQualityRuleEntity entity);

    List<DataGovernanceQualityRuleVO> convertList(List<DataGovernanceQualityRuleEntity> list);

}