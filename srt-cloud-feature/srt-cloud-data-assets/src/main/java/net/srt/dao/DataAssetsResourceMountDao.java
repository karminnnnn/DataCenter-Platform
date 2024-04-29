package net.srt.dao;

import net.srt.entity.DataAssetsResourceMountEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据资产-资源挂载表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-07-06
 */
@Mapper
public interface DataAssetsResourceMountDao extends BaseDao<DataAssetsResourceMountEntity> {

	List<DataAssetsResourceMountEntity> selectDbApplyActiveList(@Param("id") Long id, @Param("userId") Long userId);
}
