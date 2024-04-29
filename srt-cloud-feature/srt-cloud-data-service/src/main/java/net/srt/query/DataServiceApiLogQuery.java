package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;

import java.util.Date;

/**
* 数据服务-api请求日志查询
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-02-22
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据服务-api请求日志查询")
public class DataServiceApiLogQuery extends Query {
	private String ip;
	private String apiName;
}
