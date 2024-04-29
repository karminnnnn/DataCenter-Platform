package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;

/**
* 数据治理-标准码表数据查询
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-05-19
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据治理-标准码表数据查询")
public class DataGovernanceStandardCodeQuery extends Query {
	private Integer standardId;
	private String dataId;
	private String dataName;

}
