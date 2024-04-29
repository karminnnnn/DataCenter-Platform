package net.srt.api;

import net.srt.dto.SavepointDto;
import net.srt.dto.StudioExecuteDto;
import net.srt.entity.DataProductionScheduleNodeRecordEntity;
import net.srt.flink.common.result.SqlExplainResult;
import net.srt.flink.core.job.JobResult;
import net.srt.flink.gateway.result.TestResult;
import net.srt.vo.DataProductionClusterConfigurationVO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @ClassName DevelopFlinkApi
 * @Author zrx
 * @Date 2023/12/5 16:55
 */
public interface DevelopFlinkApi {

	@PostMapping(value = "/api/develop/test-gateway")
	TestResult testGateway(@RequestBody DataProductionClusterConfigurationVO vo);

	@PostMapping(value = "/api/develop/explain-flink-sql")
	List<SqlExplainResult> explainFlinkSql(@RequestBody StudioExecuteDto studioExecuteDto);

	@PostMapping(value = "/api/develop/execute-flink-sql")
	JobResult executeFlinkSql(@RequestBody StudioExecuteDto studioExecuteDto);

	@PostMapping(value = "/api/develop/submit-task")
	JobResult submitTask(@RequestBody StudioExecuteDto studioExecuteDto);

	@PostMapping(value = "/api/develop/schedule-task")
	JobResult scheduleTask(@RequestBody DataProductionScheduleNodeRecordEntity nodeRecord);

	@PostMapping(value = "/api/develop/savepoint")
	Boolean savepointJobInstance(@RequestBody SavepointDto savepointDto);
}
