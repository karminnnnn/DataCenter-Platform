package net.srt.api.module.data.integrate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;
@Data
@Schema(description = "数据集成-贴源数据")
public class DataFieldDto implements Serializable{
    private static final long serialVersionUID = 1L;
    @Schema(description = "字段表主键ID")
    private Long fieldId;

    @Schema(description = "字段名")
    private String fieldName;

    @Schema(description = "注释")
    private String remarks;

    @Schema(description = "字段类型")
    private String fieldTypeName;

    @Schema(description = "数据长度")
    private Long displaySize;

    @Schema(description = "小数位数")
    private Long scaleSize;

    @Schema(description = "默认值")
    private String defaultValue;

    @Schema(description = "是否为空")
    private Boolean nullable;

    @Schema(description = "是否主键")
    private Boolean pk;

    @Schema(description = "是否自动排序")
    private Boolean autoIncrement;

    @Schema(description = "所属数据表id")
    private Long datatableId;

    @Schema(description = "所属数据表名")
    private String datatableName;

}
