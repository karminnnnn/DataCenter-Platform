package net.srt.system.convert;

import net.srt.system.entity.SysLogLoginEntity;
import net.srt.system.entity.SysLogMetadataActiveEntity;
import net.srt.system.entity.SysLogSysActiveEntity;
import net.srt.system.entity.SysLogUserActiveEntity;
import net.srt.system.vo.SysLogLoginVO;
import net.srt.system.vo.SysLogVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SysLogUserConvert {

    SysLogUserConvert INSTANCE = Mappers.getMapper(SysLogUserConvert.class);
    List<SysLogVo> convertList(List<SysLogUserActiveEntity> list);
    List<SysLogVo> convertList2(List<SysLogSysActiveEntity> list);
    List<SysLogVo> convertList3(List<SysLogMetadataActiveEntity> list);
}
