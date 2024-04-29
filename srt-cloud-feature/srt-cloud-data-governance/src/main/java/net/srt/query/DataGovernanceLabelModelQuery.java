package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;

/**
* 数据治理-标签实体查询
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-12-24
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据治理-标签实体查询")
public class DataGovernanceLabelModelQuery extends Query {
	private String name;
}
