package net.srt.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * 数据治理-元数据采集
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-04-01
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("data_governance_metadata_collect")
public class DataGovernanceMetadataCollectEntity extends BaseEntity {

	/**
	 * 任务名称
	 */
	private String name;

	/**
	 * 数据库类型（1-数据库 2-中台库）
	 */
	private Integer dbType;

	/**
	 * 数据库主键id
	 */
	private Long databaseId;

	/**
	 * 入库策略，0-全量，1-增量
	 */
	private Integer strategy;

	/**
	 * 任务类型 1一次性 2.周期性
	 */
	private Integer taskType;

	/**
	 * cron表达式（秒 分 时 日 月 星期 年，例如 0 0 3 * * ? 表示每天凌晨三点执行）
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String cron;

	/**
	 * 归属元数据的目录
	 */
	private Long metadataId;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 是否已发布 0-否 1-是
	 */
	private Integer status;

	/**
	 * 发布时间
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Date releaseTime;

	/**
	 * 项目id
	 */
	private Long projectId;
	private Long orgId;


}
