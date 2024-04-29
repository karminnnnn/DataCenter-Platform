package net.srt.service;

import net.srt.entity.DataGovernanceQualityConfigCategoryEntity;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataGovernanceQualityConfigCategoryVO;

import java.util.List;

/**
 * 数据治理-规则配置目录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-28
 */
public interface DataGovernanceQualityConfigCategoryService extends BaseService<DataGovernanceQualityConfigCategoryEntity> {

	List<TreeNodeVo> listTree();

	void save(DataGovernanceQualityConfigCategoryVO vo);

	void update(DataGovernanceQualityConfigCategoryVO vo);

	void delete(Long id);


}
