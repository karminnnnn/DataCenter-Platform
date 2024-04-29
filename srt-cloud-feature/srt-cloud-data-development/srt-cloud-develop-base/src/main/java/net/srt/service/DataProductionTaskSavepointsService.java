package net.srt.service;

import net.srt.entity.DataProductionTaskSavepointsEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.query.DataProductionTaskSavepointsQuery;
import net.srt.vo.DataProductionTaskSavepointsVO;

import java.util.List;

/**
 * 数据生产-作业保存点
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-01-08
 */
public interface DataProductionTaskSavepointsService extends BaseService<DataProductionTaskSavepointsEntity> {

    PageResult<DataProductionTaskSavepointsVO> page(DataProductionTaskSavepointsQuery query);

    void save(DataProductionTaskSavepointsVO vo);

    void update(DataProductionTaskSavepointsVO vo);

    void delete(List<Long> idList);

	DataProductionTaskSavepointsEntity getLatestSavepointByTaskId(Long id);

	DataProductionTaskSavepointsEntity getEarliestSavepointByTaskId(Long id);
}
