package net.srt.dao;

import net.srt.entity.DataAssetsResourceOpenEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
* 数据资产-资产开放关联表
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-07-22
*/
@Mapper
public interface DataAssetsResourceOpenDao extends BaseDao<DataAssetsResourceOpenEntity> {

}
