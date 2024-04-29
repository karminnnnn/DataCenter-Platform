package net.srt.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * 数据治理-字段检测记录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-06-23
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_governance_quality_task_column")
public class DataGovernanceQualityTaskColumnEntity extends BaseEntity {

	/**
	* 质量任务id
	*/
	private Long qualityTaskId;

	/**
	* 表检测记录id
	*/
	private Long qualityTaskTableId;

	/**
	* 被检测的数据行
	*/
	private String checkRow;

	/**
	* 未通过详情
	*/
	private String notPassInfo;

	/**
	* 检测时间
	*/
	private Date checkTime;

	/**
	* 0-不通过 1-通过
	*/
	private Integer checkResult;

	/**
	* 项目id
	*/
	private Long projectId;

	private Integer deleted;

	private Integer notPassReason;



}
