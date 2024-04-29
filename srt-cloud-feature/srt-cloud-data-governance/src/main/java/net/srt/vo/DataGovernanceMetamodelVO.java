package net.srt.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
* 数据治理-元模型
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-03-28
*/
@Data
@Schema(description = "数据治理-元模型")
public class DataGovernanceMetamodelVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键id")
	private Long id;

	@Schema(description = "父id（顶级为0）")
	private Long parentId;
	private Long orgId;

	@Schema(description = "名称")
	private String name;

	@Schema(description = "代码")
	private String code;

	@Schema(description = "路径")
	private String path;

	@Schema(description = "是否内置元模型 0-否，1-是")
	private Integer builtin;

	@Schema(description = "图标")
	private String icon;

	@Schema(description = "是否是目录 0-否 1-是")
	private Integer ifLeaf;

	@Schema(description = "描述")
	private String description;

	@Schema(description = "项目id（租户id）")
	private Long projectId;

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


}
