package net.srt.convert;

import net.srt.api.module.data.governance.dto.DataGovernanceQualityTaskTableDto;
import net.srt.entity.DataGovernanceQualityTaskTableEntity;
import net.srt.vo.DataGovernanceQualityTaskTableVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 数据治理-表检测记录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-06-23
 */
@Mapper
public interface DataGovernanceQualityTaskTableConvert {
	DataGovernanceQualityTaskTableConvert INSTANCE = Mappers.getMapper(DataGovernanceQualityTaskTableConvert.class);

	DataGovernanceQualityTaskTableEntity convert(DataGovernanceQualityTaskTableVO vo);

	DataGovernanceQualityTaskTableEntity convert(DataGovernanceQualityTaskTableDto dto);

	DataGovernanceQualityTaskTableVO convert(DataGovernanceQualityTaskTableEntity entity);

	List<DataGovernanceQualityTaskTableVO> convertList(List<DataGovernanceQualityTaskTableEntity> list);

}
