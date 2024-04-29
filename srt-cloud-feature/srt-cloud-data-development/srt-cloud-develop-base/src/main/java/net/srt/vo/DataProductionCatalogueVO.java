package net.srt.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
* 数据生产目录
*
* @author zrx 985134801@qq.com
* @since 2.0.0 2022-11-27
*/
@Data
@Schema(description = "数据生产目录")
public class DataProductionCatalogueVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键id")
	private Long id;

	@Schema(description = "父级id")
	private Long parentId;
	private Long orgId;

	@Schema(description = "项目（租户）id")
	private Long projectId;

	@Schema(description = "关联的作业id")
	private Long taskId;

	@Schema(description = "名称")
	private String name;

	@Schema(description = "数据生产-作业类型（字典）")
	private Integer taskType;
	private Integer taskSqlType;

	@Schema(description = "是否可用（0-否 1-是）")
	private Integer enabled;

	@Schema(description = "是否叶子节点")
	private Integer ifLeaf;

	@Schema(description = "序号")
	private Long orderNo;

	@Schema(description = "描述")
	private String description;

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
