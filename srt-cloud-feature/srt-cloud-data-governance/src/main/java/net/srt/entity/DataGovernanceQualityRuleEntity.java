package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.api.module.data.governance.dto.quality.QualityParam;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.List;

/**
 * 数据治理-质量规则
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-28
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName(value = "data_governance_quality_rule", autoResultMap = true)
public class DataGovernanceQualityRuleEntity extends BaseEntity {

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 英文名称
	 */
	private String engName;

	/**
	 * 1-唯一性 2-规范性 3-有效性 4-完整性 5-一致性 6-及时性 7-准确性
	 */
	private Integer type;

	/**
	 * 字段配置 0-单选 1-多选
	 */
	private Integer ifColumnMultiple;

	/**
	 * 说明
	 */
	private String description;

	/**
	 * 来源 1-内置
	 */
	private Integer builtIn;

	private Long projectId;
	/**
	 * 个性化参数
	 */
	@TableField(typeHandler = JacksonTypeHandler.class)
	private List<QualityParam> param;


}
