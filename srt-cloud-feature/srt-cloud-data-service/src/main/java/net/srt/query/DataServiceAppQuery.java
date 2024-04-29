package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;

/**
* 数据服务-app应用查询
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-02-16
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据服务-app应用查询")
public class DataServiceAppQuery extends Query {
	private String name;
	private String appKey;
	private Integer ifMarket;
	private Boolean ifInfo;
	private Long applyId;
}
