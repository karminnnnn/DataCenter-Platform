package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * 数据生产-作业调度记录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-01-16
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_production_schedule_record")
public class DataProductionScheduleRecordEntity extends BaseEntity {

	public static final String SCHEDULE_RECORD = "SCHEDULE_RECORD";
	//程序异常中断重新执行的情况
	public static final String SCHEDULE_RESTART_RECORD = "SCHEDULE_RESTART_RECORD";
	private String name;
	/**
	* 调度id
	*/
	private Integer taskScheduleId;

	/**
	* 项目（租户）id
	*/
	private Long projectId;
	private Long orgId;

	/**
	* 当前状态 字典 run_status
	*/
	private Integer runStatus;

	/**
	* 开始时间
	*/
	private Date startTime;

	/**
	* 结束时间
	*/
	private Date endTime;

	/**
	* 运行日志
	*/
	private String log;

	private  Integer executeType;

	private String configJson;


}
