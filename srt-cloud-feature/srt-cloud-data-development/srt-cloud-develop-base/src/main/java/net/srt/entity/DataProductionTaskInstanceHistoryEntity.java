package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

/**
 * 数据生产任务实例历史
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-20
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("data_production_task_instance_history")
public class DataProductionTaskInstanceHistoryEntity extends BaseEntity {

	/**
	 * 项目（租户）id
	 */
	private Integer projectId;
	private Long orgId;

	/**
	 * Job information json
	 */
	private String jobJson;

	/**
	 * 任务id
	 */
	private Integer taskId;

	/**
	 * 任务历史id
	 */
	private Integer historyId;

	/**
	 * error message json
	 */
	private String exceptionsJson;

	/**
	 * checkpoints json
	 */
	private String checkpointsJson;

	/**
	 * checkpoints configuration json
	 */
	private String checkpointsConfigJson;

	/**
	 * configuration
	 */
	private String configJson;

	/**
	 * Jar configuration
	 */
	private String jarJson;

	/**
	 * cluster instance configuration
	 */
	private String clusterJson;

	/**
	 * cluster config
	 */
	private String clusterConfigurationJson;

	@TableField(exist = false)
	private ObjectNode job;
	@TableField(exist = false)
	private ObjectNode exceptions;
	@TableField(exist = false)
	private ObjectNode checkpoints;
	@TableField(exist = false)
	private ObjectNode checkpointsConfig;
	@TableField(exist = false)
	private ObjectNode config;
	@TableField(exist = false)
	private ObjectNode jar;
	@TableField(exist = false)
	private ObjectNode cluster;
	@TableField(exist = false)
	private ObjectNode clusterConfiguration;
	@TableField(exist = false)
	private boolean error;

}
