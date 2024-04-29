package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

/**
 * 任务sql代码表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-05
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_production_task_statement")
public class DataProductionTaskStatementEntity extends BaseEntity {

	/**
	* 任务id
	*/
	private Long taskId;

	/**
	* 项目租户id
	*/
	private Long projectId;

	/**
	* statement set
	*/
	private String statement;







}
