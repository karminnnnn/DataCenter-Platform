package net.srt.convert;

import net.srt.api.module.data.governance.dto.DataGovernanceQualityConfigDto;
import net.srt.entity.DataGovernanceQualityConfigEntity;
import net.srt.vo.DataGovernanceQualityConfigVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 数据治理-质量规则配置
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-29
 */
@Mapper
public interface DataGovernanceQualityConfigConvert {
	DataGovernanceQualityConfigConvert INSTANCE = Mappers.getMapper(DataGovernanceQualityConfigConvert.class);

	DataGovernanceQualityConfigEntity convert(DataGovernanceQualityConfigVO vo);

	DataGovernanceQualityConfigVO convert(DataGovernanceQualityConfigEntity entity);

	List<DataGovernanceQualityConfigVO> convertList(List<DataGovernanceQualityConfigEntity> list);

	DataGovernanceQualityConfigDto convertDto(DataGovernanceQualityConfigEntity entity);
}
