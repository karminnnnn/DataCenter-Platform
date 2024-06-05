package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据集成-贴源数据查询")
public class TableDataQuery extends Query{
    @Schema(description = "数据表主键ID")
    private Long datatableId;

    @Schema(description = "数据行列表")
    private List<Map<String, Object>> rows;
}
