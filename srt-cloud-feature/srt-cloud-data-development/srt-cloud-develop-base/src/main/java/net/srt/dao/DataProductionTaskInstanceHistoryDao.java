package net.srt.dao;

import net.srt.entity.DataProductionTaskInstanceHistoryEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
* 数据生产任务实例历史
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2022-12-20
*/
@Mapper
public interface DataProductionTaskInstanceHistoryDao extends BaseDao<DataProductionTaskInstanceHistoryEntity> {

}
