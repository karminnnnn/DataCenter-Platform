package net.srt.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
* 数据治理-数据标准
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-05-05
*/
@Data
@Schema(description = "数据治理-数据标准")
public class DataGovernanceStandardVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键id")
	private Long id;

	@Schema(description = "所属目录id")
	private Long categoryId;

	@Schema(description = "标准英文名称")
	private String engName;

	@Schema(description = "标准中文名称")
	private String cnName;

	@Schema(description = "编码数")
	private Integer codeNum;

	@Schema(description = "数据类型 数字，字符串，日期，小数")
	private String dataType;

	@Schema(description = "长度")
	private Integer dataLength;

	@Schema(description = "精度")
	private Integer dataPrecision;

	@Schema(description = "非空 0-否 1-是")
	private Integer nullable;

	@Schema(description = "标准码表id")
	private Integer standardCodeId;

	@Schema(description = "1-标准字段 2-标准码表")
	private Integer type;

	@Schema(description = "描述")
	private String note;

	@Schema(description = "项目（租户）id")
	private Long projectId;

	@Schema(description = "0-已下线 1-已上线")
	private Integer status;

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

	private Boolean ifStandardRel;


}
