package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * 数据治理-元数据采集任务记录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-04-04
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("data_governance_metadata_collect_record")
public class DataGovernanceMetadataCollectRecordEntity extends BaseEntity {

	/**
	 * 采集任务id
	 */
	private Long metadataCollectId;

	/**
	 * 1-成功 0-失败 2-运行中
	 */
	private Integer status;

	/**
	 * 实时日志
	 */
	private String realTimeLog;

	/**
	 * 错误日志
	 */
	private String errorLog;

	/**
	 * 开始时间
	 */
	private Date startTime;

	/**
	 * 结束时间
	 */
	private Date endTime;

	/**
	 * 项目（租户）id
	 */
	private Long projectId;
	private Long orgId;


}
