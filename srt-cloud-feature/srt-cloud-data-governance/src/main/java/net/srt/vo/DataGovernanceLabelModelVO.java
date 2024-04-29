package net.srt.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 数据治理-标签实体
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-12-24
 */
@Data
@Schema(description = "数据治理-标签实体")
public class DataGovernanceLabelModelVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键id")
	private Long id;

	@Schema(description = "名称")
	private String name;

	@Schema(description = "说明")
	private String description;

	@Schema(description = "模型类型 1-表 2-自定义sql")
	private Integer type;

	@Schema(description = "表名")
	private String tableName;
	private List<String> tableNameAll;

	@Schema(description = "sql语句")
	private String sqlText;

	@Schema(description = "版本号")
	private Integer version;

	@Schema(description = "删除标识  0：正常   1：已删除")
	private Integer deleted;

	@Schema(description = "创建者")
	private Long creator;

	@Schema(description = "项目id")
	private Long projectId;

	@Schema(description = "创建时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date createTime;

	@Schema(description = "机构id")
	private Long orgId;

	@Schema(description = "更新者")
	private Long updater;

	@Schema(description = "更新时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date updateTime;


}
