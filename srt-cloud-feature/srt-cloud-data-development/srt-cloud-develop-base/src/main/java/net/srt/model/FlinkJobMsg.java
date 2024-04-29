package net.srt.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName JobErrorJson
 * @Author zrx
 * @Date 2023/1/6 14:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlinkJobMsg {
	private String sql;
	private String jid;
	private JsonNode json;
}
