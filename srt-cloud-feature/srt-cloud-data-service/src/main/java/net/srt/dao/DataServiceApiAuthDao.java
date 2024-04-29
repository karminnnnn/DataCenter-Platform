package net.srt.dao;

import net.srt.entity.DataServiceApiAuthEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 数据服务-权限关联表
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-02-16
*/
@Mapper
public interface DataServiceApiAuthDao extends BaseDao<DataServiceApiAuthEntity> {

	void increaseRequestedTimes(@Param("id") Long id);

	void increaseRequestedSuccessTimes(Long id);

	void increaseRequestedFailedTimes(Long id);

	void resetRequested(Long authId);
}
