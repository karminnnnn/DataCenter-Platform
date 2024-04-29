package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;

import java.util.Date;

/**
* 数据治理-数据标准查询
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-05-05
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据治理-数据标准查询")
public class DataGovernanceStandardQuery extends Query {
	private Integer categoryId;
	private String engName;
	private String cnName;
	private Integer type;
	//如果是元数据查询，此参数为true
	private boolean ifMeta;
	private Long metadataId;
}
