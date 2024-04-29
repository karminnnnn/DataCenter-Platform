package net.srt.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
* 数据资产-资产目录
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-07-04
*/
@Data
@Schema(description = "数据资产-资产目录")
public class DataAssetsCatalogVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键id")
	private Long id;

	@Schema(description = "父级id")
	private Long parentId;

	@Schema(description = "类型-0-文件夹 1-资产目录")
	private Integer type;

	@Schema(description = "目录名称")
	private String name;

	@Schema(description = "类目代码")
	private String code;

	@Schema(description = "序号")
	private Integer orderNo;

	@Schema(description = "描述")
	private String description;

	@Schema(description = "目录全路径")
	private String path;

	@Schema(description = "项目id")
	private Long projectId;
	private Long orgId;

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
