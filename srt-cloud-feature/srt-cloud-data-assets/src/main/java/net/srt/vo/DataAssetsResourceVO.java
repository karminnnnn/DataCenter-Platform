package net.srt.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
* 数据资产-资产列表
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-07-06
*/
@Data
@Schema(description = "数据资产-资产列表")
public class DataAssetsResourceVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键id")
	private Long id;

	@Schema(description = "所属目录id")
	private Long catalogId;

	@Schema(description = "名称")
	private String name;

	@Schema(description = "编码")
	private String code;

	@Schema(description = "摘要")
	private String summary;

	@Schema(description = "0-未上架 1-已上架")
	private Integer status;

	@Schema(description = "0-未挂载 1-已挂载")
	private Integer mountStatus;

	@Schema(description = "1-全部 2-角色 3-用户")
	private Integer openType;

	@Schema(description = "角色id列表（若开放范围为角色则有此字段）")
	private List<Integer> openRoles;

	@Schema(description = "用户id列表（若开放范围为用户则有此字段）")
	private List<Integer>  openUsers;

	@Schema(description = "版本号")
	private String versionNo;

	@Schema(description = "负责人")
	private String dutyUser;

	@Schema(description = "负责人联系方式")
	private String dutyPhone;

	@Schema(description = "发布时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date releaseTime;

	@Schema(description = "发布人id")
	private Integer releaseUserId;

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
