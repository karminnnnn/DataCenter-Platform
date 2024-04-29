package net.srt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @ClassName ScheduleFlowDto
 * @Author zrx
 * @Date 2023/1/15 11:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Flow {
	private Long id;
	private Long orgId;
	private Integer recordId;
	private Integer ifCycle;
	private String name;
	private String cron;
	private String note;
	private Integer status;
	private Date releaseTime;
	private Integer releaseUserId;
	private List<FlowNode> nodes;
	private List<FlowEdge> edges;
}
