package net.srt.service;

import net.srt.api.module.data.development.constant.ExecuteType;
import net.srt.dto.Flow;
import net.srt.entity.DataProductionScheduleEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.model.ConsoleLog;
import net.srt.query.DataProductionScheduleQuery;
import net.srt.query.DataProductionScheduleRecordQuery;
import net.srt.vo.DataProductionScheduleNodeRecordVO;
import net.srt.vo.DataProductionScheduleRecordVO;
import net.srt.vo.DataProductionScheduleVO;

import java.util.List;

/**
 * 数据生产-作业调度
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-01-12
 */
public interface DataProductionScheduleService extends BaseService<DataProductionScheduleEntity> {

    PageResult<DataProductionScheduleVO> page(DataProductionScheduleQuery query);

    void save(Flow flow);

	Flow get(Long id);

	String scheduleRun(Integer id, ExecuteType executeType);

	void delete(List<Long> idList);

	String run(Integer id);

	ConsoleLog getLog(Integer recordId);

	List<DataProductionScheduleNodeRecordVO> listNodeRecord(Integer recordId);

	PageResult<DataProductionScheduleRecordVO> pageRecord(DataProductionScheduleRecordQuery query);

	void delRecord(List<Long> idList);

	DataProductionScheduleNodeRecordVO getNodeRecord(Integer nodeRecordId);

	void release(Integer id);

	void cancle(Integer id);

	void runActive();
}
