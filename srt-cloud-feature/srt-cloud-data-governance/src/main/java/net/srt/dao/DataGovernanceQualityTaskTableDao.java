package net.srt.dao;

import net.srt.entity.DataGovernanceQualityTaskTableEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
* 数据治理-表检测记录
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-06-23
*/
@Mapper
public interface DataGovernanceQualityTaskTableDao extends BaseDao<DataGovernanceQualityTaskTableEntity> {

}
