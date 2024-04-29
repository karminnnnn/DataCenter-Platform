package net.srt.dao;

import net.srt.entity.DataServiceApiLogEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
* 数据服务-api请求日志
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-02-22
*/
@Mapper
public interface DataServiceApiLogDao extends BaseDao<DataServiceApiLogEntity> {

}
