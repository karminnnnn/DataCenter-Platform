package net.srt.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.Date;


/**
 * 数据服务-权限关联表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-02-16
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_service_api_auth")
public class DataServiceApiAuthEntity extends BaseEntity {

	/**
	* app的id
	*/
	private Long appId;

	/**
	* 分组id
	*/
	private Long groupId;

	/**
	* api的id
	*/
	private Long apiId;

	/**
	* 调用次数 不限次数为-1
	*/
	private Integer requestTimes;

	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Date startTime;
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Date endTime;

	/**
	* 已调用次数
	*/
	@TableField(updateStrategy = FieldStrategy.NEVER)
	private Integer requestedTimes;
	@TableField(updateStrategy = FieldStrategy.NEVER)
	private Integer requestedSuccessTimes;
	@TableField(updateStrategy = FieldStrategy.NEVER)
	private Integer requestedFailedTimes;

	/**
	* 所属项目id
	*/
	private Long projectId;

	/**
	 * 真删
	 */
	@TableField(fill = FieldFill.INSERT)
	private Integer deleted;





}
