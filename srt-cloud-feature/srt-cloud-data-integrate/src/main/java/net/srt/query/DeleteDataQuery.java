package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据集成-贴源数据查询")
public class DeleteDataQuery {
    private Long datatableId;
    private List<List<Object>> idList;
}
