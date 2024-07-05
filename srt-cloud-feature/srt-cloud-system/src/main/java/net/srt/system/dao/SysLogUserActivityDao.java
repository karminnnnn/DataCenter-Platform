package net.srt.system.dao;

import net.srt.framework.mybatis.dao.BaseDao;
import net.srt.system.entity.SysLogLoginEntity;
import net.srt.system.entity.SysLogUserActiveEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysLogUserActivityDao extends BaseDao<SysLogUserActiveEntity> {
}
