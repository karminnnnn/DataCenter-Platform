package net.srt.service;

import net.srt.entity.DataGovernanceMetadataPropertyEntity;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataGovernanceMetadataPropertyVO;

import java.util.List;

/**
 * 数据治理-元数据属性值
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-03-29
 */
public interface DataGovernanceMetadataPropertyService extends BaseService<DataGovernanceMetadataPropertyEntity> {

	void save(DataGovernanceMetadataPropertyVO vo);

	void update(DataGovernanceMetadataPropertyVO vo);

	void delete(List<Long> idList);
}
