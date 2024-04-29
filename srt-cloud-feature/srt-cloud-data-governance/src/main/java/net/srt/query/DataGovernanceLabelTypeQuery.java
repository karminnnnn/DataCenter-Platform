package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;

import java.util.Date;

/**
* 数据治理-标签类型查询
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-12-24
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据治理-标签类型查询")
public class DataGovernanceLabelTypeQuery extends Query {
	private String name;
}
