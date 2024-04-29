package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;

/**
* 数据服务-api配置查询
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-01-28
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据服务-api配置查询")
public class DataServiceApiConfigQuery extends Query {
	private Long groupId;
	private Long resourceId;
	private Long appId;
	private String name;
	private String path;
	private String contentType;
	private Integer status;
	private Integer sqlDbType;
	private Long databaseId;
	private Integer previlege;
	private Integer openTrans;
	private Integer queryApply;
	private Integer ifMarket;
}
