package net.srt.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
* 数据集市-资源申请表
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-07-26
*/
@Data
@Schema(description = "数据集市-资源申请表")
public class DataMarketResourceApplyVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键id")
	private Long id;
	private String title;

	@Schema(description = "被申请的资源id")
	private Long resourceId;

	@Schema(description = "被申请的挂载资源id")
	private Long resourceMountId;

	@Schema(description = "1-数据库表 2-api 3-文件")
	private Integer mountType;
	private Integer mountId;
	private Long appId;

	@Schema(description = "电话")
	private String phone;

	@Schema(description = "邮箱")
	private String email;

	@Schema(description = "开始时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date applyStartTime;

	@Schema(description = "结束时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date applyEndTime;

	@Schema(description = "调用类型 0-不限 1-指定")
	private Integer applyUseType;
	@Schema(description = "调用次数（api时有此值）")
	private Integer applyUseTimes;

	@Schema(description = "申请人id")
	private Integer applyUserId;
	private String applyNote;
	private Integer checkUserId;
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date checkTime;
	private String checkNote;

	@Schema(description = "0-待审核 1-已通过 2-未通过")
	private Integer status;

	@Schema(description = "是否已授权（0-否 1-是）")
	private Integer ifAuth;

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

	@TableField(exist = false)
	private String resourceName;
	@TableField(exist = false)
	private String resourceMountName;
	@Schema(defaultValue = "是否有效 0-否 1是")
	private boolean effective;


}
