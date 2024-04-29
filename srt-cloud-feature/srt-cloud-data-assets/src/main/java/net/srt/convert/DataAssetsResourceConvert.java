package net.srt.convert;

import net.srt.entity.DataAssetsResourceEntity;
import net.srt.vo.DataAssetsResourceVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据资产-资产列表
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-07-06
*/
@Mapper
public interface DataAssetsResourceConvert {
    DataAssetsResourceConvert INSTANCE = Mappers.getMapper(DataAssetsResourceConvert.class);

    DataAssetsResourceEntity convert(DataAssetsResourceVO vo);

    DataAssetsResourceVO convert(DataAssetsResourceEntity entity);

    List<DataAssetsResourceVO> convertList(List<DataAssetsResourceEntity> list);

}