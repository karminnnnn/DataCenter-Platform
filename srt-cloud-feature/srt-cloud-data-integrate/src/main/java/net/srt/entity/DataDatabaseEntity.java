package net.srt.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * 数据集成-数据库管理
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-10-09
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName(value = "data_database", autoResultMap = true)
public class DataDatabaseEntity extends BaseEntity {

	/**
	 * 库名
	 */
	private String databaseName;

	/**
	 * 状态
	 */
	private Integer status;

	/**
	 * 同步状态
	 */
	private Integer synStatus;

	/**
	 * 数据源ID
	 */
	private Integer datasourceId;

	/**
	 * jdbcUrl
	 */
	private String jdbcUrl;

}
