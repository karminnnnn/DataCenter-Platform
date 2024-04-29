package net.srt.dao;

import net.srt.entity.DataProductionTaskSavepointsEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
* 数据生产-作业保存点
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-01-08
*/
@Mapper
public interface DataProductionTaskSavepointsDao extends BaseDao<DataProductionTaskSavepointsEntity> {

}
