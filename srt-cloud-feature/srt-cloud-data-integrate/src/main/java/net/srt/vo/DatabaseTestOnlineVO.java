package net.srt.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class DatabaseTestOnlineVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer datasourceId;
    private String  name;
}
