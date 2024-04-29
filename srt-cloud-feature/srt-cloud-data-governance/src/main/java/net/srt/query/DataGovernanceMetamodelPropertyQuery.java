package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;

import java.util.Date;

/**
* 数据治理-元模型属性查询
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-03-28
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据治理-元模型属性查询")
public class DataGovernanceMetamodelPropertyQuery extends Query {
	private String name;
	private String code;
	private Long metamodelId;
}
