package net.srt.convert;

import net.srt.entity.DataGovernanceStandardEntity;
import net.srt.vo.DataGovernanceStandardVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据治理-数据标准
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-05-05
*/
@Mapper
public interface DataGovernanceStandardConvert {
    DataGovernanceStandardConvert INSTANCE = Mappers.getMapper(DataGovernanceStandardConvert.class);

    DataGovernanceStandardEntity convert(DataGovernanceStandardVO vo);

    DataGovernanceStandardVO convert(DataGovernanceStandardEntity entity);

    List<DataGovernanceStandardVO> convertList(List<DataGovernanceStandardEntity> list);

}