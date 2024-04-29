package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;
import srt.cloud.framework.dbswitch.core.model.ColumnDescription;

/**
 * 数据治理-主数据模型字段
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-09-27
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("data_governance_master_column")
public class DataGovernanceMasterColumnEntity extends BaseEntity {


	public static ColumnDescription makeColumnDescription(DataGovernanceMasterColumnEntity entity) {
		ColumnDescription columnDescription = new ColumnDescription();
		columnDescription.setFieldName(entity.getName());
		columnDescription.setLabelName(entity.getName());
		columnDescription.setValType(entity.getFieldType());
		columnDescription.setLength(entity.getFieldLength());
		columnDescription.setPrecision(entity.getSacle());
		columnDescription.setNullable(entity.getNullable() == 1);
		columnDescription.setRemarks(entity.getComment());
		columnDescription.setPk(entity.getPk() == 1);
		return columnDescription;
	}

	/**
	 * 模型id
	 */
	private Long masterModelId;

	/**
	 * 字段名称
	 */
	private String name;

	/**
	 * 注释
	 */
	private String comment;

	/**
	 * 数据类型
	 */
	private Integer fieldType;

	/**
	 * 长度
	 */
	private Integer fieldLength;

	/**
	 * 小数位数
	 */
	private Integer sacle;

	/**
	 * 是否可为空 0-否 1-是
	 */
	private Integer nullable;

	/**
	 * 主键 0-否 1-是
	 */
	private Integer pk;

	private Long projectId;


}
