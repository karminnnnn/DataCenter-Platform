package net.srt.system.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;


@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "其他日志查询")
public class SysOtherLogQuery extends Query {
    @Schema(description = "用户名")
    private String userName;
}
