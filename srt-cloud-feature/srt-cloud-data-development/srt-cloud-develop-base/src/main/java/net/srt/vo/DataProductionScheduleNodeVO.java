package net.srt.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
* 数据生产-作业调度节点
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-01-12
*/
@Data
@Schema(description = "数据生产-作业调度节点")
public class DataProductionScheduleNodeVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键id")
	private Integer id;

	private Long projectId;
	private Long orgId;

	@Schema(description = "关联的调度id")
	private Integer taskScheduleId;

	@Schema(description = "节点编号")
	private String no;

	@Schema(description = "执行顺序")
	private Integer sort;

	@Schema(description = "节点名称")
	private String name;

	@Schema(description = "节点类型")
	private String type;

	@Schema(description = "横坐标")
	private Integer x;

	@Schema(description = "纵坐标")
	private Integer y;

	@Schema(description = "note")
	private String note;

	@Schema(description = "关联的作业id")
	private Integer taskId;

	@Schema(description = "作业类型")
	private Integer taskType;

	@Schema(description = "遇错是否继续")
	private Integer failGoOn;

	@Schema(description = "权重")
	private Integer weight;

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
