package net.srt.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "log vo")
public class SysLogVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String log;
    private String time;
    private String userName;
}
