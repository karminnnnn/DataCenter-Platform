package net.srt.dao;

import net.srt.entity.DataProductionCatalogueEntity;
import net.srt.entity.DataProductionTaskEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
* 数据生产任务
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2022-12-05
*/
@Mapper
public interface DataProductionTaskDao extends BaseDao<DataProductionTaskEntity> {

	void updateInfoByCatalogue(DataProductionCatalogueEntity entity);
}
