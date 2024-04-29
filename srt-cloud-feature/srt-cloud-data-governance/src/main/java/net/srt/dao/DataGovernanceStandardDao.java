package net.srt.dao;

import net.srt.entity.DataGovernanceStandardEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 数据治理-数据标准
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-05-05
*/
@Mapper
public interface DataGovernanceStandardDao extends BaseDao<DataGovernanceStandardEntity> {

	void updateCodeNumByStandardId(@Param("standardId") Long standardId);
}
