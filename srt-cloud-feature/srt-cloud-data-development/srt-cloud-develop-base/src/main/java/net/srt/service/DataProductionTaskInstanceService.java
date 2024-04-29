package net.srt.service;

import net.srt.entity.DataProductionTaskInstanceEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.query.DataProductionTaskInstanceQuery;
import net.srt.vo.DataProductionTaskInstanceVO;

import java.util.List;

/**
 * 数据生产任务实例
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-20
 */
public interface DataProductionTaskInstanceService extends BaseService<DataProductionTaskInstanceEntity> {

    PageResult<DataProductionTaskInstanceVO> page(DataProductionTaskInstanceQuery query);

    void save(DataProductionTaskInstanceVO vo);

    void update(DataProductionTaskInstanceVO vo);

    void delete(List<Long> idList);

	DataProductionTaskInstanceEntity getByIdWithoutTenant(Integer id);

	DataProductionTaskInstanceEntity getJobInstanceByTaskId(Integer id);

	List<DataProductionTaskInstanceEntity> listJobInstanceActive();
}
