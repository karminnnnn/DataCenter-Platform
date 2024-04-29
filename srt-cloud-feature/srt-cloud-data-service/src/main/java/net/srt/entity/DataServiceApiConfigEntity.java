package net.srt.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * 数据服务-api配置
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-01-28
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("data_service_api_config")
public class DataServiceApiConfigEntity extends BaseEntity {

	/**
	 * 分组id
	 */
	private Long groupId;

	/**
	 * api地址
	 */
	private String path;

	private String type;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 描述
	 */
	private String note;

	/**
	 * sql语句
	 */
	private String sqlText;

	private String sqlSeparator;
	private Integer sqlMaxRow;
	/**
	 *
	 */
	private String sqlParam;

	/**
	 * application/json 类API对应的json参数示例
	 */
	private String jsonParam;
	private String responseResult;

	/**
	 * 参数类型
	 */
	private String contentType;

	/**
	 * 是否发布 0-否 1-是
	 */
	private Integer status;

	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Date releaseTime;
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Long releaseUserId;

	/**
	 * 1-数据库 2-中台库
	 */
	private Integer sqlDbType;

	/**
	 * 数据库id
	 */
	private Long databaseId;

	/**
	 * 是否私有 0-否 1-是
	 */
	private Integer previlege;

	/**
	 * 是否开启事务 0-否 1-是
	 */
	private Integer openTrans;

	/**
	 * 项目id
	 */
	private Long projectId;
	private Long orgId;

	/**
	 * 已调用次数
	 */
	@TableField(updateStrategy = FieldStrategy.NEVER)
	private Integer requestedTimes;
	@TableField(updateStrategy = FieldStrategy.NEVER)
	private Integer requestedSuccessTimes;
	@TableField(updateStrategy = FieldStrategy.NEVER)
	private Integer requestedFailedTimes;

	@TableField(exist = false)
	private Long authId;
	@TableField(exist = false)
	private String groupPath;

}
