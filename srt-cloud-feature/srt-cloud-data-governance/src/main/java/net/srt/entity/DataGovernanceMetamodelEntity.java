package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

/**
 * 数据治理-元模型
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-03-28
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_governance_metamodel")
public class DataGovernanceMetamodelEntity extends BaseEntity {

	/**
	* 父id（顶级为0）
	*/
	private Long parentId;
	private Long orgId;

	/**
	* 名称
	*/
	private String name;

	/**
	* 代码
	*/
	private String code;

	/**
	* 路径
	*/
	private String path;

	/**
	* 是否内置元模型 0-否，1-是
	*/
	private Integer builtin;

	/**
	* 图标
	*/
	private String icon;

	/**
	* 是否是目录 0-否 1-是
	*/
	private Integer ifLeaf;

	/**
	* 描述
	*/
	private String description;

	/**
	* 项目id（租户id）
	*/
	private Long projectId;


	private Integer orderNo;




}
