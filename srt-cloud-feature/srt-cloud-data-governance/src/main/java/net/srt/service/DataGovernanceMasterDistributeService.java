package net.srt.service;

import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.query.DataGovernanceMasterDistributeQuery;
import net.srt.vo.DataGovernanceMasterDistributeVO;
import net.srt.entity.DataGovernanceMasterDistributeEntity;

import java.util.List;

/**
 * 数据治理-主数据派发
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-10-08
 */
public interface DataGovernanceMasterDistributeService extends BaseService<DataGovernanceMasterDistributeEntity> {

    PageResult<DataGovernanceMasterDistributeVO> page(DataGovernanceMasterDistributeQuery query);

    void save(DataGovernanceMasterDistributeVO vo);

    void update(DataGovernanceMasterDistributeVO vo);

    void delete(List<Long> idList);

	void release(Long id);

	void cancel(Long id);

	void handRun(Long id);
}
