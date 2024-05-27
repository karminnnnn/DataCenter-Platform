package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;


@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据集成-数据源管理查询")
public class DataSourceQuery extends Query{

    @Schema(description = "名称")
    private String name;

    @Schema(description = "数据库类型")
    private Integer databaseType;

    @Schema(description = "库名")
    private String databaseName;
    private String databaseSchema;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "是否支持实时接入")
    private Integer isRtApprove;

    @Schema(description = "所属项目")
    private Long projectId;
}
