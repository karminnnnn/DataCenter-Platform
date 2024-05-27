package net.srt.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

@Data
@Schema(description = "数据集成-数据库管理")
public class DataDatabaseVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "数据库名称")
    private String databaseName;

    @Schema(description = "同步状态")
    private Integer synStatus;

    @Schema(description = "数据源ID")
    private Integer datasourceId;

    @Schema(description = "版本号")
    private Integer version;

    @Schema(description = "删除标识  0：正常   1：已删除")
    private Integer deleted;

    @Schema(description = "创建者")
    private Long creator;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date createTime;

    @Schema(description = "更新者")
    private Long updater;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date updateTime;

    @Schema(description = "数据源名称")
    private String datasourceName;

    @Schema(description = "创建者名字")
    private String creatorName;

    @Schema(description = "更新者名字")
    private String updaterName;
}
