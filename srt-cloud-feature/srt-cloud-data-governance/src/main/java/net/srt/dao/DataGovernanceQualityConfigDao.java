package net.srt.dao;

import net.srt.entity.DataGovernanceQualityConfigEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
* 数据治理-质量规则配置
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-05-29
*/
@Mapper
public interface DataGovernanceQualityConfigDao extends BaseDao<DataGovernanceQualityConfigEntity> {

}
