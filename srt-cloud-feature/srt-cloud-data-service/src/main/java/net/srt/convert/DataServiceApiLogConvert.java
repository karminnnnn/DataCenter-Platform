package net.srt.convert;

import net.srt.entity.DataServiceApiLogEntity;
import net.srt.vo.DataServiceApiLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据服务-api请求日志
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-02-22
*/
@Mapper
public interface DataServiceApiLogConvert {
    DataServiceApiLogConvert INSTANCE = Mappers.getMapper(DataServiceApiLogConvert.class);

    DataServiceApiLogEntity convert(DataServiceApiLogVO vo);

    DataServiceApiLogVO convert(DataServiceApiLogEntity entity);

    List<DataServiceApiLogVO> convertList(List<DataServiceApiLogEntity> list);

}