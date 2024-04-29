package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;

/**
* 数据生产-集群实例查询
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2022-12-01
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据生产-集群实例查询")
public class DataProductionClusterQuery extends Query {

	private String name;
	private String alias;
}
