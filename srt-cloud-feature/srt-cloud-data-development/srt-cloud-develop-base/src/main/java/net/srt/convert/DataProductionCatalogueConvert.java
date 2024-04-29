package net.srt.convert;

import net.srt.entity.DataProductionCatalogueEntity;
import net.srt.vo.DataProductionCatalogueVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据生产目录
*
* @author zrx 985134801@qq.com
* @since 2.0.0 2022-11-27
*/
@Mapper
public interface DataProductionCatalogueConvert {
    DataProductionCatalogueConvert INSTANCE = Mappers.getMapper(DataProductionCatalogueConvert.class);

    DataProductionCatalogueEntity convert(DataProductionCatalogueVO vo);

    DataProductionCatalogueVO convert(DataProductionCatalogueEntity entity);

    List<DataProductionCatalogueVO> convertList(List<DataProductionCatalogueEntity> list);

}