package net.srt.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
* 数据集市-资源评价表
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-08-27
*/
@Data
@Schema(description = "数据集市-资源评价表")
public class DataMarketResourceCommentVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键id")
	private Long id;

	@Schema(description = "资源id")
	private Long resourceId;

	@Schema(description = "评价")
	private String comment;

	@Schema(description = "0-0星 1-1星 2-2星 3-3星 4-4星 5-5星")
	private Integer level;

	@Schema(description = "项目id")
	private Long projectId;

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
