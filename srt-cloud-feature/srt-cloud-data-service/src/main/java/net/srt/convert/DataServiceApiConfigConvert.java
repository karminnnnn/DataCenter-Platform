package net.srt.convert;

import net.srt.api.module.data.service.dto.DataServiceApiConfigDto;
import net.srt.entity.DataServiceApiConfigEntity;
import net.srt.vo.DataServiceApiConfigVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据服务-api配置
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-01-28
*/
@Mapper
public interface DataServiceApiConfigConvert {
    DataServiceApiConfigConvert INSTANCE = Mappers.getMapper(DataServiceApiConfigConvert.class);

    DataServiceApiConfigEntity convert(DataServiceApiConfigVO vo);

    DataServiceApiConfigVO convert(DataServiceApiConfigEntity entity);

	DataServiceApiConfigDto convertDto(DataServiceApiConfigEntity entity);

    List<DataServiceApiConfigVO> convertList(List<DataServiceApiConfigEntity> list);

}
