package net.srt.convert;

import net.srt.entity.DataGovernanceStandardCodeEntity;
import net.srt.vo.DataGovernanceStandardCodeVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据治理-标准码表数据
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-05-19
*/
@Mapper
public interface DataGovernanceStandardCodeConvert {
    DataGovernanceStandardCodeConvert INSTANCE = Mappers.getMapper(DataGovernanceStandardCodeConvert.class);

    DataGovernanceStandardCodeEntity convert(DataGovernanceStandardCodeVO vo);

    DataGovernanceStandardCodeVO convert(DataGovernanceStandardCodeEntity entity);

    List<DataGovernanceStandardCodeVO> convertList(List<DataGovernanceStandardCodeEntity> list);

}