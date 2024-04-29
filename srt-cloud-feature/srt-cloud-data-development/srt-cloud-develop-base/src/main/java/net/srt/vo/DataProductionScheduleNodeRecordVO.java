package net.srt.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
* 数据生产-作业调度节点记录
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-01-16
*/
@Data
@Schema(description = "数据生产-作业调度节点记录")
public class DataProductionScheduleNodeRecordVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键id")
	private Integer id;

	@Schema(description = "调度id")
	private Integer taskScheduleId;

	@Schema(description = "调度节点id")
	private Integer scheduleNodeId;

	@Schema(description = "调度节点no")
	private String scheduleNodeNo;

	@Schema(description = "调度记录id")
	private Integer scheduleRecordId;

	@Schema(description = "作业id")
	private Integer taskId;

	@Schema(description = "项目（租户）id")
	private Long projectId;
	private Long orgId;

	@Schema(description = "当前状态 字典 run_status")
	private Integer runStatus;

	@Schema(description = "开始时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date startTime;

	@Schema(description = "结束时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date endTime;

	@Schema(description = "运行日志")
	private String log;

	private  Integer executeType;

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

	@Schema(description = "以该节点为source的边的id")
	private String edgeId;

}
