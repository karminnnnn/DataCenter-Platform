package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;
import net.srt.framework.common.utils.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 数据生产任务历史查询
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据生产任务历史查询")
public class DataProductionTaskHistoryQuery extends Query {
	@Schema(description = "节点记录id")
	private Integer nodeRecordId;
	@Schema(description = "调度记录id")
	private Integer recordId;
	private Integer taskId;
	private String jobName;
	@Schema(description = "任务状态")
	private Integer status;
	@Schema(description = "flink实例状态")
	private String instanceStatus;
	private Integer sqlDbType;
	private Integer databaseId;
	@Schema(description = "任务方言（类型）")
	private Integer dialect;
	@Schema(description = "flink执行模式")
	private String type;
	private Integer clusterId;
	private Integer clusterConfigurationId;
	@DateTimeFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date startTime;
	@DateTimeFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date endTime;
	@DateTimeFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date finishTime;
}
