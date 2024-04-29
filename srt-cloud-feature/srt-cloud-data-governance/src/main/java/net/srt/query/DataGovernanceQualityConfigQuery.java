package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;

/**
* 数据治理-质量规则配置查询
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-05-29
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据治理-质量规则配置查询")
public class DataGovernanceQualityConfigQuery extends Query {
	private Long categoryId;
	private String name;
	private Integer status;
	private Integer taskType;
}
