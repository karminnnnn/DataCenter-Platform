package net.srt.convert;

import net.srt.api.module.data.governance.dto.DataGovernanceMasterDistributeDto;
import net.srt.entity.DataGovernanceMasterDistributeEntity;
import net.srt.vo.DataGovernanceMasterDistributeVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 数据治理-主数据派发
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-10-08
 */
@Mapper
public interface DataGovernanceMasterDistributeConvert {
	DataGovernanceMasterDistributeConvert INSTANCE = Mappers.getMapper(DataGovernanceMasterDistributeConvert.class);

	DataGovernanceMasterDistributeEntity convert(DataGovernanceMasterDistributeVO vo);

	DataGovernanceMasterDistributeVO convert(DataGovernanceMasterDistributeEntity entity);

	List<DataGovernanceMasterDistributeVO> convertList(List<DataGovernanceMasterDistributeEntity> list);

	DataGovernanceMasterDistributeDto convertDto(DataGovernanceMasterDistributeEntity masterDistributeEntity);
}
