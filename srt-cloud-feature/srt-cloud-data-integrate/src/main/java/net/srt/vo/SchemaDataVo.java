package net.srt.vo;


import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class SchemaDataVo {
    private Map<String, String> columns;
    private Map<String, Object> rows;
}
