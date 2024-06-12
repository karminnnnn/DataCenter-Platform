package net.srt.system.convert;

import net.srt.system.entity.SysUserActivityLogEntity;
import net.srt.system.vo.SysUserActivityLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SysUserActivityLogConvert {
    SysUserActivityLogConvert INSTANCE = Mappers.getMapper(SysUserActivityLogConvert.class);

    SysUserActivityLogEntity convert(SysUserActivityLogVO vo);

    SysUserActivityLogVO convert(SysUserActivityLogEntity entity);

    List<SysUserActivityLogVO> convertList(List<SysUserActivityLogEntity> list);
}
