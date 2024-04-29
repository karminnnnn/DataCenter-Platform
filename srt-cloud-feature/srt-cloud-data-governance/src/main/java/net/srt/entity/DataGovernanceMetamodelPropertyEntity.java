package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

/**
 * 数据治理-元模型属性
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-03-28
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_governance_metamodel_property")
public class DataGovernanceMetamodelPropertyEntity extends BaseEntity {

	/**
	* 元模型id
	*/
	private Integer metamodelId;

	/**
	* 属性名称
	*/
	private String name;

	/**
	* 属性代码
	*/
	private String code;

	/**
	* 数据类型 1-数字 2-字符串
	*/
	private Integer dataType;

	/**
	* 数据长度
	*/
	private Integer dataLength;

	/**
	* 输入控件，1-文本框
	*/
	private Integer inputType;

	/**
	* 允许为空 0-否 1-是
	*/
	private Integer nullable;

	/**
	* 是否内置 0-否 1-是
	*/
	private Integer builtin;

	/**
	* 项目id（租户id）
	*/
	private Long projectId;

	/**
	* 注释
	*/
	private String comment;

	/**
	* 序号
	*/
	private Integer orderNo;




}
