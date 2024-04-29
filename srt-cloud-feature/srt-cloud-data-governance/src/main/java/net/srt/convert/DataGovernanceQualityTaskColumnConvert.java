package net.srt.convert;

import net.srt.api.module.data.governance.dto.DataGovernanceQualityTaskColumnDto;
import net.srt.entity.DataGovernanceQualityTaskColumnEntity;
import net.srt.vo.DataGovernanceQualityTaskColumnVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据治理-字段检测记录
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-06-23
*/
@Mapper
public interface DataGovernanceQualityTaskColumnConvert {
    DataGovernanceQualityTaskColumnConvert INSTANCE = Mappers.getMapper(DataGovernanceQualityTaskColumnConvert.class);

    DataGovernanceQualityTaskColumnEntity convert(DataGovernanceQualityTaskColumnVO vo);

    DataGovernanceQualityTaskColumnVO convert(DataGovernanceQualityTaskColumnEntity entity);

    List<DataGovernanceQualityTaskColumnVO> convertList(List<DataGovernanceQualityTaskColumnEntity> list);

	List<DataGovernanceQualityTaskColumnEntity> convertListByDtos(List<DataGovernanceQualityTaskColumnDto> dtos);

}
