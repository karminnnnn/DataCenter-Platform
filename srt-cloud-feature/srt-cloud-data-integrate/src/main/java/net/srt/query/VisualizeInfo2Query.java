package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据集成-可视化接口查询")
public class VisualizeInfo2Query extends Query {
    @Schema(description = "NetId")
    private int NetId;
}
