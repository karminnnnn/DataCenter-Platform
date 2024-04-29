package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;

import java.util.Date;

/**
* 数据治理-元数据采集查询
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-04-01
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据治理-元数据采集查询")
public class DataGovernanceMetadataCollectQuery extends Query {
	private String name;
	/**
	 * 入库策略，0-全量，1-增量
	 */
	private Integer strategy;

	/**
	 * 任务类型 1一次性 2.周期性
	 */
	private Integer taskType;
}
