package net.srt.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据生产任务历史
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-19
 */
@Data
@Schema(description = "数据生产任务历史")
public class DataProductionTaskHistoryVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键id")
	private Integer id;

	@Schema(description = "项目（租户）id")
	private Integer projectId;
	private Long orgId;

	/**
	 * sql模式下的数据库类型
	 */
	private Integer sqlDbType;
	/**
	 * 数据库id
	 */
	private Integer databaseId;

	@Schema(description = "集群实例id")
	private Integer clusterId;

	@Schema(description = "集群配置id")
	private Integer clusterConfigurationId;

	@Schema(description = "session")
	private String session;

	@Schema(description = "Job ID")
	private String jobId;

	@Schema(description = "Job Name")
	private String jobName;

	@Schema(description = "JJobManager Address")
	private String jobManagerAddress;

	@Schema(description = "status")
	private Integer status;

	@Schema(description = "dialect")
	private Integer dialect;

	@Schema(description = "job type")
	private String type;

	@Schema(description = "statement set")
	private String statement;

	@Schema(description = "error message")
	private String error;

	@Schema(description = "result set")
	private String result;

	@Schema(description = "config json")
	private String configJson;

	@Schema(description = "job start time")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date startTime;

	@Schema(description = "job end time")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date endTime;

	@Schema(description = "task ID")
	private Integer taskId;

	/**
	 * 执行类型 1-手动 2-调度
	 */
	private Integer executeType;

	private Integer scheduleId;
	private Integer scheduleNodeId;
	private Integer scheduleRecordId;
	private Integer scheduleNodeRecordId;

	@Schema(description = "版本号")
	private Integer version;

	@Schema(description = "删除标识  0：正常   1：已删除")
	private Integer deleted;

	@Schema(description = "创建者")
	private Long creator;

	@Schema(description = "创建时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date createTime;

	@Schema(description = "更新者")
	private Long updater;

	@Schema(description = "更新时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date updateTime;

	@Schema(description = "实例状态")
	private String instanceStatus;

	@Schema(description = "flink实例结束时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date finishTime;

	private String executeSql;

	private String executeNo;

	private String jid;

	private String duration;


}
