package net.srt.system.dao;

import net.srt.framework.mybatis.dao.BaseDao;
import net.srt.system.entity.SysLogMetadataActiveEntity;
import net.srt.system.entity.SysLogUserActiveEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysLogMetadataActivityDao extends BaseDao<SysLogMetadataActiveEntity> {
}
