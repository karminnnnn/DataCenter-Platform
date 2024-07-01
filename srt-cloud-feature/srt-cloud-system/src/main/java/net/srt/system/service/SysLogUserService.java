package net.srt.system.service;

import lombok.AllArgsConstructor;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.system.entity.SysLogLoginEntity;
import net.srt.system.entity.SysLogSysActiveEntity;
import net.srt.system.entity.SysLogUserActiveEntity;
import net.srt.system.query.SysOtherLogQuery;
import net.srt.system.vo.SysLogVo;
import org.springframework.stereotype.Service;

public interface SysLogUserService extends BaseService<SysLogUserActiveEntity> {
    PageResult<SysLogVo> pageUser(SysOtherLogQuery query);
}
