package net.srt.system.service;

import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.system.entity.SysUserActivityLogEntity;
import net.srt.system.query.SysLogLoginQuery;
import net.srt.system.query.SysUserActivityLogQuery;
import net.srt.system.vo.SysLogLoginVO;
import net.srt.system.vo.SysUserActivityLogVO;

public interface SysUserActivityLogService extends BaseService<SysUserActivityLogEntity> {
    PageResult<SysUserActivityLogVO> page(SysUserActivityLogQuery query);

    /**
     * 保存登录日志
     *
     * @param username  用户名
     * @param action 操作类型
     */
    void save(String username,  Integer action);
}
