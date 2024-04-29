package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;

/**
* 数据生产-作业调度查询
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-01-12
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据生产-作业调度查询")
public class DataProductionScheduleQuery extends Query {
	private String name;
	private Integer status;

}
