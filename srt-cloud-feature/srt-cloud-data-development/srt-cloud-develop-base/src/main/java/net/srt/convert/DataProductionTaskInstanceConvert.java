package net.srt.convert;

import net.srt.entity.DataProductionTaskInstanceEntity;
import net.srt.vo.DataProductionTaskInstanceVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据生产任务实例
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2022-12-20
*/
@Mapper
public interface DataProductionTaskInstanceConvert {
    DataProductionTaskInstanceConvert INSTANCE = Mappers.getMapper(DataProductionTaskInstanceConvert.class);

    DataProductionTaskInstanceEntity convert(DataProductionTaskInstanceVO vo);

    DataProductionTaskInstanceVO convert(DataProductionTaskInstanceEntity entity);

    List<DataProductionTaskInstanceVO> convertList(List<DataProductionTaskInstanceEntity> list);

}