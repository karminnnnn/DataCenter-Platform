package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

/**
 * 数据治理-标准目录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-05
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_governance_standard_category")
public class DataGovernanceStandardCategoryEntity extends BaseEntity {

	/**
	* 0-普通目录 1-标准字段目录 2-标准码表目录
	*/
	private Integer type;

	/**
	* 父级id（顶级为0）
	*/
	private Long parentId;

	/**
	* 目录名称
	*/
	private String name;

	/**
	* 目录路径
	*/
	private String path;

	/**
	* 序号
	*/
	private Integer orderNo;

	/**
	* 描述
	*/
	private String note;

	/**
	* 项目(租户)id
	*/
	private Long projectId;







}
