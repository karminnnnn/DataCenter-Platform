package net.srt.dao;

import net.srt.entity.DataGovernanceMetadataPropertyEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import net.srt.vo.DataGovernanceMetamodelPropertyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据治理-元数据属性值
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-03-29
 */
@Mapper
public interface DataGovernanceMetadataPropertyDao extends BaseDao<DataGovernanceMetadataPropertyEntity> {

	List<DataGovernanceMetamodelPropertyVO> listPropertyById(@Param("id") Long id, @Param("metamodelId") Long metamodelId);
}
