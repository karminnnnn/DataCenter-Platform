package net.srt.entity;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * 数据资产-资产列表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-07-06
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName(value = "data_assets_resource", autoResultMap = true)
public class DataAssetsResourceEntity extends BaseEntity {

	/**
	 * 所属目录id
	 */
	private Long catalogId;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 编码
	 */
	private String code;

	/**
	 * 摘要
	 */
	private String summary;

	/**
	 * 0-未上架 1-已上架
	 */
	private Integer status;

	/**
	 * 0-未挂载 1-已挂载
	 */
	private Integer mountStatus;

	/**
	 * 1-全部 2-角色 3-用户
	 */
	private Integer openType;

	/**
	 * 角色id列表（若开放范围为角色则有此字段）
	 */
	@TableField(typeHandler = JacksonTypeHandler.class, updateStrategy = FieldStrategy.IGNORED)
	private List<Integer> openRoles;

	/**
	 * 用户id列表（若开放范围为用户则有此字段）
	 */
	@TableField(typeHandler = JacksonTypeHandler.class, updateStrategy = FieldStrategy.IGNORED)
	private List<Integer> openUsers;

	/**
	 * 版本号
	 */
	private String versionNo;

	/**
	 * 负责人
	 */
	private String dutyUser;

	/**
	 * 负责人联系方式
	 */
	private String dutyPhone;

	/**
	 * 发布时间
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Date releaseTime;

	/**
	 * 发布人id
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer releaseUserId;

	/**
	 * 项目id
	 */
	private Long projectId;
	private Long orgId;


}
