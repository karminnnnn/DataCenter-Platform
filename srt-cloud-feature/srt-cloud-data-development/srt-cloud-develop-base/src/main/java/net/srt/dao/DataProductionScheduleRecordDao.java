package net.srt.dao;

import net.srt.entity.DataProductionScheduleRecordEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
* 数据生产-作业调度记录
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-01-16
*/
@Mapper
public interface DataProductionScheduleRecordDao extends BaseDao<DataProductionScheduleRecordEntity> {

}
