package net.srt.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * 数据治理-质量任务
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-06-23
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_governance_quality_task")
public class DataGovernanceQualityTaskEntity extends BaseEntity {

	/**
	* 规则配置id
	*/
	private Long qualityConfigId;

	/**
	* 名称
	*/
	private String name;

	/**
	* 运行状态（ 1-等待中 2-运行中 3-正常结束 4-异常结束）
	*/
	private Integer status;

	/**
	* 检测条数
	*/
	private Integer checkCount;

	/**
	* 检测通过数
	*/
	private Integer passCount;

	/**
	* 未通过数
	*/
	private Integer notPassCount;

	/**
	* 开始时间
	*/
	private Date startTime;

	/**
	* 结束时间
	*/
	private Date endTime;

	/**
	* 项目id
	*/
	private Long projectId;
	private Long orgId;


	private String errorLog;


	private Integer deleted;

}
