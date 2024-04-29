package net.srt.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
* 数据资产-资源挂载表
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-07-06
*/
@Data
@Schema(description = "数据资产-资源挂载表")
public class DataAssetsResourceMountVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键id")
	private Long id;

	@Schema(description = "资源id")
	private Long resourceId;

	@Schema(description = "1-数据库表 2-api 3-文件")
	private Integer mountType;

	@Schema(description = "挂载的数据id（元数据，api，文件）")
	private Long mountId;
	@Schema(description = "挂载的资源名称")
	private String mountName;
	/**
	 * 项目id
	 */
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
	@Schema(description = "状态 1-有效 0-失效")
	private Integer status;
	@Schema(description = "检测日志")
	private String checkLog;

}
