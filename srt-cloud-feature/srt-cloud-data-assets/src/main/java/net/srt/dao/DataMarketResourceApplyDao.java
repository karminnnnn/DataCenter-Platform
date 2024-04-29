package net.srt.dao;

import net.srt.entity.DataMarketResourceApplyEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* 数据集市-资源申请表
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-07-26
*/
@Mapper
public interface DataMarketResourceApplyDao extends BaseDao<DataMarketResourceApplyEntity> {

	List<DataMarketResourceApplyEntity> getList(Map<String, Object> params);
}
