package net.srt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @ClassName FlowNodeDto
 * @Author zrx
 * @Date 2023/1/15 11:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowNode {
	private String id;
	private String type;
	private Integer x;
	private Integer y;
	private FlowNodeProperties properties;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		FlowNode flowNode = (FlowNode) o;
		return Objects.equals(id, flowNode.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
