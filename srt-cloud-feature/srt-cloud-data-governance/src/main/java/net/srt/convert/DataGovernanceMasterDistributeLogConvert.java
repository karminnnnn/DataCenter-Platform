package net.srt.convert;

import net.srt.api.module.data.governance.dto.DataGovernanceMasterDistributeLogDto;
import net.srt.entity.DataGovernanceMasterDistributeLogEntity;
import net.srt.vo.DataGovernanceMasterDistributeLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据治理-主数据派发日志
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-10-08
*/
@Mapper
public interface DataGovernanceMasterDistributeLogConvert {
    DataGovernanceMasterDistributeLogConvert INSTANCE = Mappers.getMapper(DataGovernanceMasterDistributeLogConvert.class);

    DataGovernanceMasterDistributeLogEntity convert(DataGovernanceMasterDistributeLogVO vo);

	DataGovernanceMasterDistributeLogEntity convert(DataGovernanceMasterDistributeLogDto dto);

    DataGovernanceMasterDistributeLogVO convert(DataGovernanceMasterDistributeLogEntity entity);

    List<DataGovernanceMasterDistributeLogVO> convertList(List<DataGovernanceMasterDistributeLogEntity> list);

}
