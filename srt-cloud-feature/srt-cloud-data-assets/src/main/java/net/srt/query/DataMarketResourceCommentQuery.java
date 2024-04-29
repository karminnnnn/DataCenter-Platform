package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;

/**
* 数据集市-资源评价表查询
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-08-27
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据集市-资源评价表查询")
public class DataMarketResourceCommentQuery extends Query {
	private Long resourceId;
	private Integer level;
	private String comment;
}
