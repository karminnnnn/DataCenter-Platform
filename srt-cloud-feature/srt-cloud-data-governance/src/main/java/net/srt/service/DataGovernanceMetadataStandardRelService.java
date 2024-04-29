package net.srt.service;

import net.srt.entity.DataGovernanceMetadataStandardRelEntity;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataGovernanceMetadataStandardRelVO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 数据治理-元数据标准关联表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-23
 */
public interface DataGovernanceMetadataStandardRelService extends BaseService<DataGovernanceMetadataStandardRelEntity> {

	void save(DataGovernanceMetadataStandardRelVO vo);

	void update(DataGovernanceMetadataStandardRelVO vo);

	void delete(Long metadataId, Long standardId);

	DataGovernanceMetadataStandardRelVO getMetadataRel(Long metadataId);
}
