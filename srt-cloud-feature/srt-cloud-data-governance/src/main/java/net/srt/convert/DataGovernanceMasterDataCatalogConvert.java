package net.srt.convert;

import net.srt.entity.DataGovernanceMasterDataCatalogEntity;
import net.srt.vo.DataGovernanceMasterDataCatalogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据治理-主数据目录
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-09-27
*/
@Mapper
public interface DataGovernanceMasterDataCatalogConvert {
    DataGovernanceMasterDataCatalogConvert INSTANCE = Mappers.getMapper(DataGovernanceMasterDataCatalogConvert.class);

    DataGovernanceMasterDataCatalogEntity convert(DataGovernanceMasterDataCatalogVO vo);

    DataGovernanceMasterDataCatalogVO convert(DataGovernanceMasterDataCatalogEntity entity);

    List<DataGovernanceMasterDataCatalogVO> convertList(List<DataGovernanceMasterDataCatalogEntity> list);

}