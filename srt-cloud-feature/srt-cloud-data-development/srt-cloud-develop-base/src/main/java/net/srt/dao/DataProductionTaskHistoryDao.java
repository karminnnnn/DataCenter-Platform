package net.srt.dao;

import net.srt.entity.DataProductionTaskHistoryEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* 数据生产任务历史
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2022-12-19
*/
@Mapper
public interface DataProductionTaskHistoryDao extends BaseDao<DataProductionTaskHistoryEntity> {

	List<DataProductionTaskHistoryEntity> getHistoryList(Map<String, Object> params);
}
