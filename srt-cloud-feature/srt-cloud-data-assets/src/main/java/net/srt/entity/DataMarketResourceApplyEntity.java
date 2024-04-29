package net.srt.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * 数据集市-资源申请表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-07-26
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("data_market_resource_apply")
public class DataMarketResourceApplyEntity extends BaseEntity {

	/**
	 * 被申请的资源id
	 */
	private Long resourceId;
	private String title;

	/**
	 * 被申请的挂载资源id
	 */
	private Long resourceMountId;

	/**
	 * 1-数据库表 2-api 3-文件
	 */
	private Integer mountType;

	private Integer mountId;
	private Long appId;
	/**
	 * 电话
	 */
	private String phone;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 开始时间
	 */
	private Date applyStartTime;

	/**
	 * 结束时间
	 */
	private Date applyEndTime;

	/**
	 * 调用类型（0-不限次数 1-指定次数）
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer applyUseType;
	/**
	 * 调用次数（api时有此值）
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer applyUseTimes;

	/**
	 * 申请人id
	 */
	private Integer applyUserId;

	private String applyNote;
	private Integer checkUserId;
	private Date checkTime;
	private String checkNote;

	/**
	 * 0-待审核 1-已通过 2-未通过
	 */
	private Integer status;

	/**
	 * 是否已授权（0-否 1-是）
	 */
	private Integer ifAuth;

	/**
	 * 项目id
	 */
	private Long projectId;

	@TableField(exist = false)
	private String resourceName;
	@TableField(exist = false)
	private String resourceMountName;


}
