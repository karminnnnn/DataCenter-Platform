package net.srt.convert;

import net.srt.entity.DataAssetsCatalogEntity;
import net.srt.vo.DataAssetsCatalogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据资产-资产目录
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-07-04
*/
@Mapper
public interface DataAssetsCatalogConvert {
    DataAssetsCatalogConvert INSTANCE = Mappers.getMapper(DataAssetsCatalogConvert.class);

    DataAssetsCatalogEntity convert(DataAssetsCatalogVO vo);

    DataAssetsCatalogVO convert(DataAssetsCatalogEntity entity);

    List<DataAssetsCatalogVO> convertList(List<DataAssetsCatalogEntity> list);

}