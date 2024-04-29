package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * 数据生产任务历史
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-19
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName(value = "data_production_task_history", autoResultMap = true)
public class DataProductionTaskHistoryEntity extends BaseEntity {

	/**
	 * 项目（租户）id
	 */
	private Integer projectId;
	private Long orgId;
	/**
	 * sql模式下的数据库类型
	 */
	private Integer sqlDbType;
	/**
	 * 数据库id
	 */
	private Integer databaseId;
	/**
	 * 集群实例id
	 */
	private Integer clusterId;

	/**
	 * 集群配置id
	 */
	private Integer clusterConfigurationId;

	/**
	 * session
	 */
	private String session;

	/**
	 * Job ID
	 */
	private String jobId;

	/**
	 * Job Name
	 */
	private String jobName;

	/**
	 * JJobManager Address
	 */
	private String jobManagerAddress;

	/**
	 * status
	 */
	private Integer status;

	/**
	 * 任务类型
	 */
	private Integer dialect;

	/**
	 * job type
	 */
	private String type;

	/**
	 * statement set
	 */
	private String statement;

	/**
	 * error message
	 */
	private String error;

	/**
	 * result set
	 */
	private String result;

	/**
	 * config json
	 */
	private String configJson;

	/**
	 * job start time
	 */
	private Date startTime;

	/**
	 * job end time
	 */
	private Date endTime;

	/**
	 * task ID
	 */
	private Integer taskId;

	/**
	 * 执行类型 1-手动 2-调度
	 */
	private Integer executeType;

	private Integer scheduleId;
	private Integer scheduleNodeId;
	private Integer scheduleRecordId;
	private Integer scheduleNodeRecordId;

	private String executeSql;

	private String executeNo;

	@TableField(exist = false)
	private ObjectNode config;

	@TableField(exist = false)
	private String instanceStatus;

	@TableField(exist = false)
	private Date finishTime;

	@TableField(exist = false)
	private String jid;
	@TableField(exist = false)
	private String duration;


	public DataProductionTaskInstanceEntity buildJobInstance() {
		DataProductionTaskInstanceEntity jobInstance = new DataProductionTaskInstanceEntity();
		jobInstance.setHistoryId(super.getId().intValue());
		jobInstance.setClusterId(clusterId);
		jobInstance.setTaskId(taskId);
		jobInstance.setName(jobName);
		return jobInstance;
	}
}
