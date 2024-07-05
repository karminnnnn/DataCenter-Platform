package net.srt.system.service;

import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.system.entity.SysLogSysActiveEntity;
import net.srt.system.entity.SysLogUserActiveEntity;
import net.srt.system.query.SysOtherLogQuery;
import net.srt.system.vo.SysLogVo;

public interface SysLogSysService extends BaseService<SysLogSysActiveEntity> {
    PageResult<SysLogVo> pageSys(SysOtherLogQuery query);
}
