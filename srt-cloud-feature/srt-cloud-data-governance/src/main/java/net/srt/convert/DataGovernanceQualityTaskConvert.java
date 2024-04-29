package net.srt.convert;

import net.srt.api.module.data.governance.dto.DataGovernanceQualityTaskDto;
import net.srt.entity.DataGovernanceQualityTaskEntity;
import net.srt.vo.DataGovernanceQualityTaskVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据治理-质量任务
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-06-23
*/
@Mapper
public interface DataGovernanceQualityTaskConvert {
    DataGovernanceQualityTaskConvert INSTANCE = Mappers.getMapper(DataGovernanceQualityTaskConvert.class);

    DataGovernanceQualityTaskEntity convert(DataGovernanceQualityTaskVO vo);

	DataGovernanceQualityTaskEntity convert(DataGovernanceQualityTaskDto dto);

    DataGovernanceQualityTaskVO convert(DataGovernanceQualityTaskEntity entity);

    List<DataGovernanceQualityTaskVO> convertList(List<DataGovernanceQualityTaskEntity> list);

}
