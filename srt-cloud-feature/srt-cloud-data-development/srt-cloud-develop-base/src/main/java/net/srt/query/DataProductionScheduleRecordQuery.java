package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;
import net.srt.framework.common.utils.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
* 数据生产-作业调度记录查询
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-01-16
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据生产-作业调度记录查询")
public class DataProductionScheduleRecordQuery extends Query {
	private String name;
	private Integer runStatus;
	private Integer executeType;
	@DateTimeFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date startTime;
	@DateTimeFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date endTime;
}
