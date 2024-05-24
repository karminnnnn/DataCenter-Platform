package net.srt.system.convert;

import net.srt.framework.security.user.UserDetail;
import net.srt.system.entity.SysUserEntity;
import net.srt.system.vo.SysUserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* @description: SysUser表字段转换
* @author PatrickLi 373595331@qq.com
* @date 2024/5/21
*/
@Mapper
public interface SysUserConvert {
    SysUserConvert INSTANCE = Mappers.getMapper(SysUserConvert.class);

    SysUserVO convert(SysUserEntity entity);

    SysUserEntity convert(SysUserVO vo);

    SysUserVO convert(UserDetail userDetail);

    UserDetail convertDetail(SysUserEntity entity);

    List<SysUserVO> convertList(List<SysUserEntity> list);

}
