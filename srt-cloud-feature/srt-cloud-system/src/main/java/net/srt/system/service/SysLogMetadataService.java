package net.srt.system.service;

import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.system.entity.SysLogMetadataActiveEntity;
import net.srt.system.entity.SysLogUserActiveEntity;
import net.srt.system.query.SysOtherLogQuery;
import net.srt.system.vo.SysLogVo;

public interface SysLogMetadataService extends BaseService<SysLogMetadataActiveEntity> {
    PageResult<SysLogVo> pageMetadata(SysOtherLogQuery query);
}
