package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

/**
 * 数据服务-app应用
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-02-16
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_service_app")
public class DataServiceAppEntity extends BaseEntity {

	/**
	* 名称
	*/
	private String name;

	/**
	* 备注
	*/
	private String note;

	/**
	* app_key
	*/
	private String appKey;

	/**
	* app_secret
	*/
	private String appSecret;

	/**
	* 过期描述
	*/
	private String expireDesc;

	/**
	* 过期时间  -1永久；0 单次失效；> 0 失效时间
	*/
	private Long expireDuration;

	private Integer ifMarket;

	/**
	* 所属项目id
	*/
	private Long projectId;
	private Long orgId;


}
