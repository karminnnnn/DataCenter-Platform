package net.srt.service;

import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataGovernanceMetadataCollectRecordVO;
import net.srt.query.DataGovernanceMetadataCollectRecordQuery;
import net.srt.entity.DataGovernanceMetadataCollectRecordEntity;

import java.util.List;

/**
 * 数据治理-元数据采集任务记录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-04-04
 */
public interface DataGovernanceMetadataCollectRecordService extends BaseService<DataGovernanceMetadataCollectRecordEntity> {

    PageResult<DataGovernanceMetadataCollectRecordVO> page(DataGovernanceMetadataCollectRecordQuery query);

    void save(DataGovernanceMetadataCollectRecordVO vo);

    void update(DataGovernanceMetadataCollectRecordVO vo);

    void delete(List<Long> idList);

	void dealNotFinished();
}
