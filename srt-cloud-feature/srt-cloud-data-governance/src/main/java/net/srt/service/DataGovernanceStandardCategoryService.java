package net.srt.service;

import net.srt.entity.DataGovernanceStandardCategoryEntity;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataGovernanceStandardCategoryVO;

import java.util.List;

/**
 * 数据治理-标准目录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-05
 */
public interface DataGovernanceStandardCategoryService extends BaseService<DataGovernanceStandardCategoryEntity> {

	List<TreeNodeVo> listTree();

    void save(DataGovernanceStandardCategoryVO vo);

    void update(DataGovernanceStandardCategoryVO vo);

    void delete(Long id);

}
