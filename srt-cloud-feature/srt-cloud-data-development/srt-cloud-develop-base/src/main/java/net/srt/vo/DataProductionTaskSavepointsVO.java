package net.srt.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
* 数据生产-作业保存点
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-01-08
*/
@Data
@Schema(description = "数据生产-作业保存点")
public class DataProductionTaskSavepointsVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "ID")
	private Integer id;

	@Schema(description = "task ID")
	private Integer taskId;

	private Integer historyId;

	@Schema(description = "项目（租户）id")
	private Integer projectId;

	@Schema(description = "task name")
	private String name;

	@Schema(description = "savepoint type")
	private String type;

	@Schema(description = "savepoint path")
	private String path;

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
