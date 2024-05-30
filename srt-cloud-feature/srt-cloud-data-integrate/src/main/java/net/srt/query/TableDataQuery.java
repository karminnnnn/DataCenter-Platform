package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;

import java.util.List;
import java.util.Map;

public class TableDataQuery {
    @Schema(description = "数据表主键ID")
    private Long datatableId;

    @Schema(description = "数据行列表")
    private List<Map<String, Object>> rows;
}
