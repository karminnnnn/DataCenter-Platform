package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;
import net.srt.framework.common.utils.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
* 数据集市-资源申请表查询
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-07-26
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据集市-资源申请表查询")
public class DataMarketResourceApplyQuery extends Query {
	private Long resourceId;
	private String resourceName;
	private String resourceMountName;
	private Integer mountType;
	@DateTimeFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date applyStartTime;
	@DateTimeFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date applyEndTime;
	private Integer status;
	private Integer ifAuth;
	private Integer ifSelf;
}
