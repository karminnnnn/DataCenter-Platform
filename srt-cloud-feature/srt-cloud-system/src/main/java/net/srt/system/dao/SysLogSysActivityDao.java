package net.srt.system.dao;

import net.srt.framework.mybatis.dao.BaseDao;
import net.srt.system.entity.SysLogSysActiveEntity;
import net.srt.system.entity.SysLogUserActiveEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysLogSysActivityDao extends BaseDao<SysLogSysActiveEntity> {
}
