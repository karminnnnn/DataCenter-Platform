package net.srt.service;

import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.query.DataGovernanceMetamodelPropertyQuery;
import net.srt.vo.DataGovernanceMetamodelPropertyVO;
import net.srt.entity.DataGovernanceMetamodelPropertyEntity;

import java.util.List;

/**
 * 数据治理-元模型属性
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-03-28
 */
public interface DataGovernanceMetamodelPropertyService extends BaseService<DataGovernanceMetamodelPropertyEntity> {

    PageResult<DataGovernanceMetamodelPropertyVO> page(DataGovernanceMetamodelPropertyQuery query);

    void save(DataGovernanceMetamodelPropertyVO vo);

    void update(DataGovernanceMetamodelPropertyVO vo);

    void delete(List<Long> idList);

	List<DataGovernanceMetamodelPropertyVO> properties(Long metaModelId);
}
