package net.srt.api;

import lombok.RequiredArgsConstructor;
import net.srt.dto.SavepointDto;
import net.srt.dto.StudioExecuteDto;
import net.srt.entity.DataProductionScheduleNodeRecordEntity;
import net.srt.flink.common.result.SqlExplainResult;
import net.srt.flink.core.job.JobResult;
import net.srt.flink.gateway.result.TestResult;
import net.srt.service.DataProductionClusterConfigurationService;
import net.srt.service.DataProductionTaskService;
import net.srt.vo.DataProductionClusterConfigurationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName DataAccessApiImpl
 * @Author zrx
 * @Date 2022/10/26 11:50
 */
public class DevelopFlinkApiImpl implements DevelopFlinkApi {

	@Autowired
	private DataProductionClusterConfigurationService clusterConfigurationService;
	@Autowired
	private DataProductionTaskService taskService;

	@Override
	public TestResult testGateway(DataProductionClusterConfigurationVO clusterConfiguration) {
		return clusterConfigurationService.testGatewayCommon(clusterConfiguration);
	}

	@Override
	public List<SqlExplainResult> explainFlinkSql(StudioExecuteDto studioExecuteDto) {
		return taskService.explainFlinkSqlCommon(studioExecuteDto);
	}

	@Override
	public JobResult executeFlinkSql(StudioExecuteDto studioExecuteDto) {
		return taskService.executeFlinkSqlCommon(studioExecuteDto);
	}

	@Override
	public JobResult submitTask(StudioExecuteDto studioExecuteDto) {
		return taskService.submitTaskCommon(studioExecuteDto);
	}

	@Override
	public JobResult scheduleTask(DataProductionScheduleNodeRecordEntity nodeRecord) {
		return taskService.scheduleTaskCommon(nodeRecord);
	}

	@Override
	public Boolean savepointJobInstance(SavepointDto savepointDto) {
		return taskService.savepointJobInstance(savepointDto.getInstanceId(), savepointDto.getHistoryId(), savepointDto.getSavePointType());
	}
}
