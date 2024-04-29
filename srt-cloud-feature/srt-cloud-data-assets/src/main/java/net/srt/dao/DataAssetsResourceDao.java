package net.srt.dao;

import net.srt.entity.DataAssetsResourceEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* 数据资产-资产列表
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-07-06
*/
@Mapper
public interface DataAssetsResourceDao extends BaseDao<DataAssetsResourceEntity> {

	List<DataAssetsResourceEntity> getMarketList(Map<String, Object> params);
}
