package net.srt.convert;

import net.srt.entity.DataServiceApiGroupEntity;
import net.srt.vo.DataServiceApiGroupVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据服务-api分组
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-01-28
*/
@Mapper
public interface DataServiceApiGroupConvert {
    DataServiceApiGroupConvert INSTANCE = Mappers.getMapper(DataServiceApiGroupConvert.class);

    DataServiceApiGroupEntity convert(DataServiceApiGroupVO vo);

    DataServiceApiGroupVO convert(DataServiceApiGroupEntity entity);

    List<DataServiceApiGroupVO> convertList(List<DataServiceApiGroupEntity> list);

}