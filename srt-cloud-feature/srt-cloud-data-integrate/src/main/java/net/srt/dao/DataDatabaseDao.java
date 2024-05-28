package net.srt.dao;

import net.srt.entity.DataDatabaseEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DataDatabaseDao extends BaseDao<DataDatabaseEntity> {
    String getUsernameById(@Param("id") Long id);

    void changeStatusByName(@Param("databaseName")String databaseName,@Param("status") Integer status);
}
