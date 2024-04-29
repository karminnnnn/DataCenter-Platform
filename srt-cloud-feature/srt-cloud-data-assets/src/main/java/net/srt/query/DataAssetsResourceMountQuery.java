package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;

import java.util.Date;

/**
* 数据资产-资源挂载表查询
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-07-06
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据资产-资源挂载表查询")
public class DataAssetsResourceMountQuery extends Query {
	private Long resourceId;
	private String mountName;
}
