package net.srt.convert;

import net.srt.api.module.data.service.dto.DataServiceApiAuthDto;
import net.srt.entity.DataServiceApiAuthEntity;
import net.srt.vo.DataServiceApiAuthVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据服务-权限关联表
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-02-16
*/
@Mapper
public interface DataServiceApiAuthConvert {
    DataServiceApiAuthConvert INSTANCE = Mappers.getMapper(DataServiceApiAuthConvert.class);

    DataServiceApiAuthEntity convert(DataServiceApiAuthVO vo);

    DataServiceApiAuthVO convert(DataServiceApiAuthEntity entity);

    List<DataServiceApiAuthVO> convertList(List<DataServiceApiAuthEntity> list);

	DataServiceApiAuthEntity convertDto(DataServiceApiAuthDto apiAuthDto);
}
