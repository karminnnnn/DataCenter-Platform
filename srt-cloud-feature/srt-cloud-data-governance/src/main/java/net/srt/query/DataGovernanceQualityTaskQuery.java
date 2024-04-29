package net.srt.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;
import net.srt.framework.common.utils.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 数据治理-质量任务查询
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-06-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据治理-质量任务查询")
public class DataGovernanceQualityTaskQuery extends Query {
	private String name;
	private Integer status;
	@DateTimeFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date startTime;
	@DateTimeFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date endTime;
}
