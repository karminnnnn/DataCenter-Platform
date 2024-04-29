package net.srt.convert;

import net.srt.entity.DataProductionClusterEntity;
import net.srt.vo.DataProductionClusterVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据生产-集群实例
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2022-12-01
*/
@Mapper
public interface DataProductionClusterConvert {
    DataProductionClusterConvert INSTANCE = Mappers.getMapper(DataProductionClusterConvert.class);

    DataProductionClusterEntity convert(DataProductionClusterVO vo);

    DataProductionClusterVO convert(DataProductionClusterEntity entity);

    List<DataProductionClusterVO> convertList(List<DataProductionClusterEntity> list);

}