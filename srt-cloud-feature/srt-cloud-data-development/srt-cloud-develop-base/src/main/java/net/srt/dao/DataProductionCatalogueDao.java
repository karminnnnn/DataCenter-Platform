package net.srt.dao;

import net.srt.entity.DataProductionCatalogueEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 数据生产目录
 *
 * @author zrx 985134801@qq.com
 * @since 2.0.0 2022-11-27
 */
@Mapper
public interface DataProductionCatalogueDao extends BaseDao<DataProductionCatalogueEntity> {

	void updateTaskIdById(@Param("taskId") Long taskId, @Param("catalogueId") Long catalogueId);
}
