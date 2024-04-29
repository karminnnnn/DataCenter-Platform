package net.srt.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.common.query.Query;

import java.util.Date;

/**
* 数据资产-资产列表查询
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-07-06
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据资产-资产列表查询")
public class DataAssetsResourceQuery extends Query {
	private Long catalogId;
	private String name;
	private String code;
	private Integer status;
	private Integer mountStatus;
	private String dutyUser;
}
