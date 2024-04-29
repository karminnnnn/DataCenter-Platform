package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.api.module.data.governance.dto.quality.QulaityColumn;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * 数据治理-表检测记录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-06-23
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName(value = "data_governance_quality_task_table", autoResultMap = true)
public class DataGovernanceQualityTaskTableEntity extends BaseEntity {

	/**
	 * 质量任务id
	 */
	private Long qualityTaskId;

	/**
	 * 被检测的表id
	 */
	private Long tableMetadataId;

	/**
	 * 被检测的表
	 */
	private String tableName;


	@TableField(typeHandler = JacksonTypeHandler.class)
	private List<QulaityColumn> columnInfo;
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
	 * 检测时间
	 */
	private Date checkTime;

	/**
	 * 开始时间
	 */
	private Date startTime;

	/**
	 * 结束时间
	 */
	private Date endTime;


	private String errorLog;


	/**
	 * 运行状态（ 1-等待中 2-运行中 3-正常结束 4-异常结束）
	 */
	private Integer status;

	/**
	 * 项目id
	 */
	private Long projectId;

	private Integer deleted;


}
