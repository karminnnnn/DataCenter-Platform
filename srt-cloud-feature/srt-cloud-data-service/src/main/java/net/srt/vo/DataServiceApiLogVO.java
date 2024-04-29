package net.srt.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
* 数据服务-api请求日志
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-02-22
*/
@Data
@Schema(description = "数据服务-api请求日志")
public class DataServiceApiLogVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键id")
	private Long id;

	@Schema(description = "url")
	private String url;

	@Schema(description = "响应状态码")
	private Integer status;

	@Schema(description = "时长")
	private Long duration;

	@Schema(description = "IP地址")
	private String ip;

	@Schema(description = "app的id")
	private String appId;

	@Schema(description = "api的id")
	private String apiId;

	private String appName;
	private String apiName;

	@Schema(description = "错误信息")
	private String error;

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
