package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;

/**
* 数据生产-集群配置查询
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2022-12-20
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据生产-集群配置查询")
public class DataProductionClusterConfigurationQuery extends Query {

	private String name;
	private String alias;
}
