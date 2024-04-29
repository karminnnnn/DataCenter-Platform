package net.srt.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.List;

/**
 * 数据治理-标签实体
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-12-24
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName(value = "data_governance_label_model", autoResultMap = true)
public class DataGovernanceLabelModelEntity extends BaseEntity {

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 说明
	 */
	private String description;

	/**
	 * 模型类型 1-表 2-自定义sql
	 */
	private Integer type;

	/**
	 * 表名
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String tableName;

	@TableField(typeHandler = JacksonTypeHandler.class, updateStrategy = FieldStrategy.IGNORED)
	private List<String> tableNameAll;

	/**
	 * sql语句
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String sqlText;


	/**
	 * 项目id
	 */
	private Long projectId;


	/**
	 * 机构id
	 */
	private Long orgId;


}
