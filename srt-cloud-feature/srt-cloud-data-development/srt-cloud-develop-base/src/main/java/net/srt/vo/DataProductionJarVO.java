package net.srt.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
* 数据生产-jar管理
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-11-13
*/
@Data
@Schema(description = "数据生产-jar管理")
public class DataProductionJarVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键id")
	private Long id;

	@Schema(description = "项目（租户id）")
	private Long projectId;

	private Long orgId;

	@Schema(description = "名称")
	private String name;

	private Integer submitType;

	@Schema(description = "文件地址")
	private String path;

	@Schema(description = "主类")
	private String mainClass;

	@Schema(description = "参数")
	private String params;

	@Schema(description = "备注")
	private String note;

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
