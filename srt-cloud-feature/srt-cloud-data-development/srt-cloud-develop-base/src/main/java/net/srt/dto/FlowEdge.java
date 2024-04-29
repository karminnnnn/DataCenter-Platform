package net.srt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @ClassName FlowEdge
 * @Author zrx
 * @Date 2023/1/15 12:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowEdge {
	private String id;
	private String type;
	private Map<String, Object> startPoint;
	private Map<String, Object> endPoint;
	private List<Map<String, Object>> pointsList;
	private String sourceNodeId;
	private String targetNodeId;
	private Map<String, Object> properties;

}
