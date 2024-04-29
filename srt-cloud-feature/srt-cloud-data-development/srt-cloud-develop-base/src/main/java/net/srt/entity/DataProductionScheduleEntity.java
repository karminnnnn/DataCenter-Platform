package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * 数据生产-作业调度
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-01-12
 */
@EqualsAndHashCode(callSuper = false)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "data_production_schedule", autoResultMap = true)
public class DataProductionScheduleEntity extends BaseEntity {


	private Long projectId;
	private Long orgId;
	/**
	 * 调度名称
	 */
	private String name;

	private Integer ifCycle;

	/**
	 * cron表达式
	 */
	private String cron;

	/**
	 * 描述
	 */
	private String note;

	/**
	 * 0-未发布 1-已发布
	 */
	private Integer status;

	private Date releaseTime;
	private Integer releaseUserId;

	/**
	 * 节点关系的json
	 */
	private String edges;


}
