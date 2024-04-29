package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * 数据治理-主数据派发日志
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-10-08
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("data_governance_master_distribute_log")
public class DataGovernanceMasterDistributeLogEntity extends BaseEntity {

	/**
	 * 派发id
	 */
	private Long distributeId;

	/**
	 * 主数据id
	 */
	private Long masterModelId;

	/**
	 * 运行状态（ 1-等待中 2-运行中 3-正常结束 4-异常结束）
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
	 * 错误信息
	 */
	private String errorInfo;

	/**
	 * 派发数据量
	 */
	private Long dataCount;

	/**
	 * 数据量大小
	 */
	private String byteCount;

	/**
	 * 项目id（租户id）
	 */
	private Long projectId;
	private Long orgId;


}
