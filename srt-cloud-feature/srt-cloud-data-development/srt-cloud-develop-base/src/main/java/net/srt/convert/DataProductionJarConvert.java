package net.srt.convert;

import net.srt.entity.DataProductionJarEntity;
import net.srt.vo.DataProductionJarVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据生产-jar管理
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-11-13
*/
@Mapper
public interface DataProductionJarConvert {
    DataProductionJarConvert INSTANCE = Mappers.getMapper(DataProductionJarConvert.class);

    DataProductionJarEntity convert(DataProductionJarVO vo);

    DataProductionJarVO convert(DataProductionJarEntity entity);

    List<DataProductionJarVO> convertList(List<DataProductionJarEntity> list);

}