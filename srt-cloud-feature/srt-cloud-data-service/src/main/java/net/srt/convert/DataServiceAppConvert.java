package net.srt.convert;

import net.srt.entity.DataServiceAppEntity;
import net.srt.vo.DataServiceAppVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据服务-app应用
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-02-16
*/
@Mapper
public interface DataServiceAppConvert {
    DataServiceAppConvert INSTANCE = Mappers.getMapper(DataServiceAppConvert.class);

    DataServiceAppEntity convert(DataServiceAppVO vo);

    DataServiceAppVO convert(DataServiceAppEntity entity);

    List<DataServiceAppVO> convertList(List<DataServiceAppEntity> list);

}