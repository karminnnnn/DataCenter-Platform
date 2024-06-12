package net.srt.system.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;
import net.srt.framework.common.utils.DateUtils;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户活动日志查询")
public class SysUserActivityLogQuery extends Query {
    @Schema(description = "用户名")
    private String username;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date time;
}
