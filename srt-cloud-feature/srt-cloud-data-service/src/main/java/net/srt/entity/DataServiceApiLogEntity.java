package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

/**
 * 数据服务-api请求日志
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-02-22
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_service_api_log")
public class DataServiceApiLogEntity extends BaseEntity {

	/**
	* url
	*/
	private String url;

	/**
	* 响应状态码
	*/
	private Integer status;

	/**
	* 时长
	*/
	private Long duration;

	/**
	* IP地址
	*/
	private String ip;

	/**
	* app的id
	*/
	private Long appId;

	/**
	* api的id
	*/
	private Long apiId;

	private String appName;
	private String apiName;

	/**
	* 错误信息
	*/
	private String error;

	/**
	* 项目id
	*/
	private Long projectId;
	private Long orgId;







}
