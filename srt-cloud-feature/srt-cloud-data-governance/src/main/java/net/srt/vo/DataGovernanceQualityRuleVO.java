package net.srt.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.srt.api.module.data.governance.dto.quality.QualityParam;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
* 数据治理-质量规则
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-05-28
*/
@Data
@Schema(description = "数据治理-质量规则")
public class DataGovernanceQualityRuleVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键id")
	private Long id;

	@Schema(description = "名称")
	private String name;

	@Schema(description = "英文名称")
	private String engName;

	@Schema(description = "1-唯一性 2-规范性 3-有效性 4-完整性 5-一致性 6-及时性 7-准确性")
	private Integer type;

	@Schema(description = "字段配置 0-单选 1-多选")
	private Integer ifColumnMultiple;

	@Schema(description = "说明")
	private String description;

	@Schema(description = "来源 1-内置")
	private Integer builtIn;
	private Long projectId;

	@Schema(description = "个性化参数")
	private List<QualityParam> param;

	@Schema(description = "版本号")
	private Integer version;

	@Schema(description = "删除标识  0：正常   1：已删除")
	private Integer deleted;

	@Schema(description = "创建者")
	private Long creator;

	@Schema(description = "创建时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date createTime;

	@Schema(description = "更新者")
	private Long updater;

	@Schema(description = "更新时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date updateTime;


}
