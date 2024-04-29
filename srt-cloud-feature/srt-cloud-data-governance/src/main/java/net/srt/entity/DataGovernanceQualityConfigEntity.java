package net.srt.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.api.module.data.governance.dto.quality.QualityConfigParam;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.List;

/**
 * 数据治理-质量规则配置
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-29
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName(value = "data_governance_quality_config", autoResultMap = true)
public class DataGovernanceQualityConfigEntity extends BaseEntity {

	/**
	 * 名称
	 */
	private String name;

	private Long categoryId;

	/**
	 * 规则id
	 */
	private Integer ruleId;

	/**
	 * 个性化参数json
	 */
	@TableField(typeHandler = JacksonTypeHandler.class)
	private QualityConfigParam param;

	/**
	 * 元数据字段列表
	 */
	@TableField(typeHandler = JacksonTypeHandler.class)
	private List<Integer> metadataIds;

	/**
	 * 状态，1-启用，0-关闭
	 */
	private Integer status;

	/**
	 * 任务类型，1-一次性任务，2-周期任务
	 */
	private Integer taskType;

	/**
	 * cron表达式
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String cron;

	/**
	 * 备注
	 */
	private String note;

	/**
	 * 项目id
	 */
	private Long projectId;
	private Long orgId;


}
