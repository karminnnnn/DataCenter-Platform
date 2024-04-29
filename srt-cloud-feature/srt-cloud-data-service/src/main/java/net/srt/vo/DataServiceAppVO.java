package net.srt.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
* 数据服务-app应用
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-02-16
*/
@Data
@Schema(description = "数据服务-app应用")
public class DataServiceAppVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键id")
	private Long id;

	@Schema(description = "名称")
	private String name;

	@Schema(description = "备注")
	private String note;

	@Schema(description = "app_key")
	private String appKey;

	@Schema(description = "app_secret")
	private String appSecret;

	@Schema(description = "过期描述")
	private String expireDesc;

	/**
	 * 过期时间  -1永久；0 单次失效；> 0 失效时间
	 */
	private Long expireDuration;

	private Integer ifMarket;

	@Schema(description = "所属项目id")
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
