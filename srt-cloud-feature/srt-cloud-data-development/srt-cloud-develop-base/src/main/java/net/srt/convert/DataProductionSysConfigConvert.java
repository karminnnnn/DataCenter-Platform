package net.srt.convert;

import net.srt.entity.DataProductionSysConfigEntity;
import net.srt.vo.DataProductionSysConfigVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据生产-配置中心
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2022-12-26
*/
@Mapper
public interface DataProductionSysConfigConvert {
    DataProductionSysConfigConvert INSTANCE = Mappers.getMapper(DataProductionSysConfigConvert.class);

    DataProductionSysConfigEntity convert(DataProductionSysConfigVO vo);

    DataProductionSysConfigVO convert(DataProductionSysConfigEntity entity);

    List<DataProductionSysConfigVO> convertList(List<DataProductionSysConfigEntity> list);

}