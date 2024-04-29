package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;

/**
* 数据治理-主数据派发查询
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-10-08
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据治理-主数据派发查询")
public class DataGovernanceMasterDistributeQuery extends Query {
	private String name;
	private Integer distributeType;
	private Integer status;
}
