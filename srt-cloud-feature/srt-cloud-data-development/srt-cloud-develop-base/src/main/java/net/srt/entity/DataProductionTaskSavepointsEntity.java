package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

/**
 * 数据生产-作业保存点
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-01-08
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("data_production_task_savepoints")
public class DataProductionTaskSavepointsEntity extends BaseEntity {

	/**
	 * task ID
	 */
	private Integer taskId;

	private Integer historyId;

	/**
	 * 项目（租户）id
	 */
	private Integer projectId;

	/**
	 * task name
	 */
	private String name;

	/**
	 * savepoint type
	 */
	private String type;

	/**
	 * savepoint path
	 */
	private String path;


}
