package net.srt.service;

import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.query.DataGovernanceMetadataCollectQuery;
import net.srt.vo.DataGovernanceMetadataCollectVO;
import net.srt.entity.DataGovernanceMetadataCollectEntity;

import java.util.List;

/**
 * 数据治理-元数据采集
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-04-01
 */
public interface DataGovernanceMetadataCollectService extends BaseService<DataGovernanceMetadataCollectEntity> {

    PageResult<DataGovernanceMetadataCollectVO> page(DataGovernanceMetadataCollectQuery query);

    void save(DataGovernanceMetadataCollectVO vo);

    void update(DataGovernanceMetadataCollectVO vo);

    void delete(List<Long> idList);

	void release(Long id);

	void cancel(Long id);

	void handRun(Long id);
}
