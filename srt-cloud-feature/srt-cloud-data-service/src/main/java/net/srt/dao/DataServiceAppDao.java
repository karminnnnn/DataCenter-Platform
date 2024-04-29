package net.srt.dao;

import net.srt.entity.DataServiceAppEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 数据服务-app应用
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-02-16
*/
@Mapper
public interface DataServiceAppDao extends BaseDao<DataServiceAppEntity> {

	DataServiceAppEntity selectByApplyId(@Param("applyId") Long applyId);
}
