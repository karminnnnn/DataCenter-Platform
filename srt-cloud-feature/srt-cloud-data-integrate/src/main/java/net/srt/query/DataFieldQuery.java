package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据集成-贴源数据查询")
public class DataFieldQuery extends Query {
   // @Schema(description = "表名")
   // private String datatableName;

    @Schema(description = "所属数据表id")
    private Long datatableId;
}
