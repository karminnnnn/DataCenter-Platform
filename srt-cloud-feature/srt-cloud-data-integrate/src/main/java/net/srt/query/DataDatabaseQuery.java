package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;

/**
* 数据集成-数据库管理查询
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2022-10-09
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据集成-数据库管理查询")
public class DataDatabaseQuery extends Query {
    @Schema(description = "名称")
    private String name;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "数据源ID")
    private Integer datasourceId;

    @Schema(description = "创建者名称")
    private String creatorName;

}
