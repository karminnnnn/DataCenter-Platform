package net.srt.dao;

import net.srt.entity.DataProductionTaskStatementEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
* 任务sql代码表
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2022-12-05
*/
@Mapper
public interface DataProductionTaskStatementDao extends BaseDao<DataProductionTaskStatementEntity> {

	void updataByTaskId(DataProductionTaskStatementEntity statement);
}
