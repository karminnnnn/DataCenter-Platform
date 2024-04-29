package net.srt.convert;

import net.srt.entity.DataAssetsResourceMountEntity;
import net.srt.vo.DataAssetsResourceMountVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据资产-资源挂载表
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-07-06
*/
@Mapper
public interface DataAssetsResourceMountConvert {
    DataAssetsResourceMountConvert INSTANCE = Mappers.getMapper(DataAssetsResourceMountConvert.class);

    DataAssetsResourceMountEntity convert(DataAssetsResourceMountVO vo);

    DataAssetsResourceMountVO convert(DataAssetsResourceMountEntity entity);

    List<DataAssetsResourceMountVO> convertList(List<DataAssetsResourceMountEntity> list);

}