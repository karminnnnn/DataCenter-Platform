package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * 数据生产任务实例 与 DataProductionTaskInstanceHistoryEntity 一对一
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-20
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName(value = "data_production_task_instance", autoResultMap = true)
public class DataProductionTaskInstanceEntity extends BaseEntity {

	/**
	 * 任务实例名称
	 */
	private String name;

	/**
	 * 项目（租户）id
	 */
	private Integer projectId;
	private Long orgId;

	/**
	 * 任务id
	 */
	private Integer taskId;

	/**
	 * job lifecycle
	 */
	private Integer step;

	/**
	 * 集群实例id
	 */
	private Integer clusterId;

	/**
	 * Flink JobId
	 */
	private String jid;

	/**
	 * job instance status
	 */
	private String status;

	/**
	 * execution history ID
	 */
	private Integer historyId;

	/**
	 * finish time
	 */
	private Date finishTime;

	/**
	 * job duration
	 */
	private Long duration;

	/**
	 * error logs
	 */
	private String error;

	/**
	 * failed restart count
	 */
	private Integer failedRestartCount;

	@TableField(exist = false)
	private String taskType;

	private String executeSql;

	private String executeNo;


}
