package net.srt.convert;

import net.srt.entity.DataProductionClusterConfigurationEntity;
import net.srt.vo.DataProductionClusterConfigurationVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据生产-集群配置
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2022-12-20
*/
@Mapper
public interface DataProductionClusterConfigurationConvert {
    DataProductionClusterConfigurationConvert INSTANCE = Mappers.getMapper(DataProductionClusterConfigurationConvert.class);

    DataProductionClusterConfigurationEntity convert(DataProductionClusterConfigurationVO vo);

    DataProductionClusterConfigurationVO convert(DataProductionClusterConfigurationEntity entity);

    List<DataProductionClusterConfigurationVO> convertList(List<DataProductionClusterConfigurationEntity> list);

}