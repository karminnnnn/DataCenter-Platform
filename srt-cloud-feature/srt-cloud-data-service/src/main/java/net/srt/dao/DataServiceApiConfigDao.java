package net.srt.dao;

import net.srt.entity.DataServiceApiConfigEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 数据服务-api配置
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-01-28
*/
@Mapper
public interface DataServiceApiConfigDao extends BaseDao<DataServiceApiConfigEntity> {

	List<DataServiceApiConfigEntity> getAuthList(Map<String, Object> params);

	List<DataServiceApiConfigEntity> getResourceList(Map<String, Object> params);

	void increaseRequestedTimes(@Param("id") Long id);

	void increaseRequestedSuccessTimes(Long id);

	void increaseRequestedFailedTimes(Long id);


}
