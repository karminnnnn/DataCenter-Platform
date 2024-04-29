package net.srt.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
* 数据治理-元模型属性
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-03-28
*/
@Data
@Schema(description = "数据治理-元模型属性")
public class DataGovernanceMetamodelPropertyVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键id")
	private Long id;

	@Schema(description = "元模型id")
	private Integer metamodelId;

	@Schema(description = "属性名称")
	private String name;

	@Schema(description = "属性代码")
	private String code;

	@Schema(description = "数据类型 1-数字 2-字符串")
	private Integer dataType;

	@Schema(description = "数据长度")
	private Integer dataLength;

	@Schema(description = "输入控件，1-文本框")
	private Integer inputType;

	@Schema(description = "允许为空 0-否 1-是")
	private Integer nullable;

	@Schema(description = "是否内置 0-否 1-是")
	private Integer builtin;

	@Schema(description = "项目id（租户id）")
	private Long projectId;

	@Schema(description = "注释")
	private String comment;

	@Schema(description = "序号")
	private Integer orderNo;

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

	@Schema(description = "元模型属性id")
	private Long metamodelPropertyId;
	@Schema(description = "元数据的属性值")
	private String value;
	@Schema(description = "元数据属性的主键id")
	private Long metadataPropertyId;

}
