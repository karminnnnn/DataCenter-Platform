package net.srt.dao;

import net.srt.entity.DataProductionTaskInstanceEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 数据生产任务实例
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2022-12-20
*/
@Mapper
public interface DataProductionTaskInstanceDao extends BaseDao<DataProductionTaskInstanceEntity> {

	DataProductionTaskInstanceEntity getJobInstanceByTaskId(@Param("id") Integer id);

	List<DataProductionTaskInstanceEntity> listJobInstanceActive();
}
