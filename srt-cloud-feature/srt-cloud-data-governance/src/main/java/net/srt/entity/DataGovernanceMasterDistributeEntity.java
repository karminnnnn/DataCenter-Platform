package net.srt.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.api.module.data.governance.dto.distribute.DistributeJson;
import net.srt.api.module.data.governance.dto.distribute.IncrField;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * 数据治理-主数据派发
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-10-08
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName(value = "data_governance_master_distribute", autoResultMap = true)
public class DataGovernanceMasterDistributeEntity extends BaseEntity {

	/**
	 * 主数据id
	 */
	private Long masterModelId;

	private String name;

	/**
	 * 派发类型 1-数据库 2-接口 3-消息队列
	 */
	private Integer distributeType;

	/**
	 * 派发json配置（不同类型json配置不同）
	 */
	@TableField(typeHandler = JacksonTypeHandler.class)
	private DistributeJson distributeJson;

	/**
	 * 上次执行到的增量记录
	 */
	@TableField(typeHandler = JacksonTypeHandler.class)
	private IncrField incrField;

	/**
	 * 状态 0-未发布 1-已发布
	 */
	private Integer status;

	/**
	 * 任务类型 2-一次性全量同步 3-一次性全量周期性增量
	 */
	private Integer taskType;

	/**
	 * cron表达式
	 */
	private String cron;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 发布时间
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Date releaseTime;

	/**
	 * 发布人id
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Long releaseUserId;

	/**
	 * 项目id（租户id）
	 */
	private Long projectId;
	private Long orgId;


}
