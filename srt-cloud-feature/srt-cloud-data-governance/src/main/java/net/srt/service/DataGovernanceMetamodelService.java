package net.srt.service;

import net.srt.entity.DataGovernanceMetamodelEntity;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataGovernanceMetamodelVO;

import java.util.List;

/**
 * 数据治理-元模型
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-03-28
 */
public interface DataGovernanceMetamodelService extends BaseService<DataGovernanceMetamodelEntity> {

	List<TreeNodeVo> listTree();

    void save(DataGovernanceMetamodelVO vo);

    void update(DataGovernanceMetamodelVO vo);

    void delete(Long id);

}
