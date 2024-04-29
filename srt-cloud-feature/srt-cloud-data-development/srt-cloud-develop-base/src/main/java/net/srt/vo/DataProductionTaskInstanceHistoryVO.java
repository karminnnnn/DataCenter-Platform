package net.srt.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
* 数据生产任务实例历史
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2022-12-20
*/
@Data
@Schema(description = "数据生产任务实例历史")
public class DataProductionTaskInstanceHistoryVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "id")
	private Integer id;

	@Schema(description = "项目（租户）id")
	private Integer projectId;
	private Long orgId;

	@Schema(description = "任务id")
	private Integer taskId;

	@Schema(description = "任务历史id")
	private Integer historyId;

	@Schema(description = "Job information json")
	private String jobJson;

	@Schema(description = "error message json")
	private String exceptionsJson;

	@Schema(description = "checkpoints json")
	private String checkpointsJson;

	@Schema(description = "checkpoints configuration json")
	private String checkpointsConfigJson;

	@Schema(description = "configuration")
	private String configJson;

	@Schema(description = "Jar configuration")
	private String jarJson;

	@Schema(description = "cluster instance configuration")
	private String clusterJson;

	@Schema(description = "cluster config")
	private String clusterConfigurationJson;

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


}
