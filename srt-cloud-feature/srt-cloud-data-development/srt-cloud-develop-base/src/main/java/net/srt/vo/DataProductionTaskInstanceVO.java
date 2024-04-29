package net.srt.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
* 数据生产任务实例
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2022-12-20
*/
@Data
@Schema(description = "数据生产任务实例")
public class DataProductionTaskInstanceVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "id")
	private Integer id;

	@Schema(description = "任务实例名称")
	private String name;

	@Schema(description = "项目（租户）id")
	private Integer projectId;
	private Long orgId;

	@Schema(description = "任务id")
	private Integer taskId;

	@Schema(description = "job lifecycle")
	private Integer step;

	@Schema(description = "集群实例id")
	private Integer clusterId;

	@Schema(description = "Flink JobId")
	private String jid;

	@Schema(description = "job instance status")
	private String status;

	@Schema(description = "execution history ID")
	private Integer historyId;

	@Schema(description = "finish time")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date finishTime;

	@Schema(description = "job duration")
	private Long duration;

	@Schema(description = "error logs")
	private String error;

	@Schema(description = "failed restart count")
	private Integer failedRestartCount;

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

	private String executeSql;

	private String executeNo;

}
