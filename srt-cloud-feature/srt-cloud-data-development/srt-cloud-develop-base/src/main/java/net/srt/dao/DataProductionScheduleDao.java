package net.srt.dao;

import net.srt.entity.DataProductionScheduleEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
* 数据生产-作业调度
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-01-12
*/
@Mapper
public interface DataProductionScheduleDao extends BaseDao<DataProductionScheduleEntity> {

	void changeStutus(DataProductionScheduleEntity dbEntity);
}
