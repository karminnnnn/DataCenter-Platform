package net.srt.service;

import net.srt.entity.DataGovernanceMetadataEntity;
import net.srt.framework.common.cache.bean.Neo4jInfo;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataGovernanceMetadataVO;

import java.util.List;

/**
 * 数据治理-元数据
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-03-29
 */
public interface DataGovernanceMetadataService extends BaseService<DataGovernanceMetadataEntity> {

    void save(DataGovernanceMetadataVO vo);

    void update(DataGovernanceMetadataVO vo);

    void delete(Long id);

	List<TreeNodeVo> listByPatentId(Long parentId);

	List<TreeNodeVo> listDb();

	DataGovernanceMetadataVO get(Long id);

	List<TreeNodeVo> listByKeyword(String keyword);

	List<TreeNodeVo> listFloder();

	void deleteAll(Long id);

	void upNeo4jInfo(Neo4jInfo neo4jInfo);

	Neo4jInfo getNeo4jInfo();

}
