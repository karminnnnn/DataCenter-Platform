package net.srt.dao;

import net.srt.entity.DataGovernanceMetadataCollectRecordEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
* 数据治理-元数据采集任务记录
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-04-04
*/
@Mapper
public interface DataGovernanceMetadataCollectRecordDao extends BaseDao<DataGovernanceMetadataCollectRecordEntity> {

}
