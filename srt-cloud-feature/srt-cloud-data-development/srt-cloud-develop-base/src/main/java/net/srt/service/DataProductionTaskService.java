package net.srt.service;

import net.srt.dto.StudioExecuteDto;
import net.srt.entity.DataProductionCatalogueEntity;
import net.srt.entity.DataProductionScheduleNodeRecordEntity;
import net.srt.entity.DataProductionTaskEntity;
import net.srt.entity.DataProductionTaskInstanceEntity;
import net.srt.flink.common.model.JobStatus;
import net.srt.flink.common.result.SqlExplainResult;
import net.srt.flink.core.job.JobResult;
import net.srt.flink.core.result.SelectResult;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.model.ConsoleLog;
import net.srt.model.JobInfoDetail;
import net.srt.query.DataProductionTaskHistoryQuery;
import net.srt.query.DataProductionTaskSavepointsQuery;
import net.srt.vo.DataProductionTaskHistoryVO;
import net.srt.vo.DataProductionTaskInstanceHistoryVO;
import net.srt.vo.DataProductionTaskSavepointsVO;
import net.srt.vo.DataProductionTaskVO;

import java.util.List;

/**
 * 数据生产任务
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-05
 */
public interface DataProductionTaskService extends BaseService<DataProductionTaskEntity> {

	DataProductionTaskVO save(DataProductionTaskVO vo);

	DataProductionTaskVO update(DataProductionTaskVO vo);

	void delete(Long id);

	DataProductionTaskVO get(Long id);

	List<SqlExplainResult> explainSql(StudioExecuteDto studioExecuteDto);

	JobResult executeSql(StudioExecuteDto studioExecuteDto);

	JobResult justExecuteSql(StudioExecuteDto studioExecuteDto);

	DataProductionTaskInstanceEntity refreshJobInstance(Integer id, boolean isCoercive);

	JobStatus checkJobStatus(JobInfoDetail jobInfoDetail);

	DataProductionTaskEntity getTaskInfoById(Integer id);

	void handleJobDone(DataProductionTaskInstanceEntity jobInstance);

	JobResult submitTaskCommon(StudioExecuteDto studioExecuteDto);

	JobResult submitTask(Long taskId);

	JobResult scheduleTaskCommon(DataProductionScheduleNodeRecordEntity nodeRecord);

	JobResult scheduleTask(DataProductionScheduleNodeRecordEntity nodeRecord);

	ConsoleLog getConsoleLog();

	void clearLog();

	void endConsoleLog();

	SelectResult getJobDataByJobId(String jobId);

	void updateInfoByCatalogue(DataProductionCatalogueEntity entity);

	void delHistory(List<Long> idList);

	PageResult<DataProductionTaskHistoryVO> pageHistory(DataProductionTaskHistoryQuery query);

	String getInstanceError(Integer historyId);

	DataProductionTaskInstanceHistoryVO getHistoryClusterInfo(Integer historyId);

	void clearLogWithOutKey();

	void savepoint(Integer historyId, String type);

	boolean savepointTask(Integer historyId, String savePointType);

	PageResult<DataProductionTaskSavepointsVO> pageSavepoint(DataProductionTaskSavepointsQuery query);

	void delSavepoint(List<Long> idList);

	List<DataProductionTaskVO> listEnv();

	boolean savepointJobInstance(Integer jobInstanceId, Integer historyId, String savePointType);

	JobResult executeFlinkSqlCommon(StudioExecuteDto studioExecuteDto);

	List<SqlExplainResult> explainFlinkSqlCommon(StudioExecuteDto studioExecuteDto);

}
