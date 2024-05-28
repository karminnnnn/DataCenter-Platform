package net.srt.convert;

import net.srt.api.module.data.integrate.dto.DataDatabaseDto;
import net.srt.api.module.data.integrate.dto.DataSourceDto;
import net.srt.entity.DataSourceEntity;
import net.srt.vo.DataSourceVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据集成-数据库管理
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2022-10-09
*/
@Mapper
public interface DataSourceConvert {
    DataSourceConvert INSTANCE = Mappers.getMapper(DataSourceConvert.class);  // 创建一个代理实例

    DataSourceEntity convert(DataSourceVO vo);

    DataSourceVO convert(DataSourceEntity entity);

	DataSourceDto convertDto(DataSourceEntity entity);

    List<DataSourceVO> convertList(List<DataSourceEntity> list);

}
