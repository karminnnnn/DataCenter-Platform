package net.srt.dao;

import net.srt.entity.DataGovernanceMetadataEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
* 数据治理-元数据
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-03-29
*/
@Mapper
public interface DataGovernanceMetadataDao extends BaseDao<DataGovernanceMetadataEntity> {

}
