package net.srt.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

/**
 * 数据治理-元数据属性值
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-03-29
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_governance_metadata_property")
public class DataGovernanceMetadataPropertyEntity extends BaseEntity {

	/**
	* 属性id
	*/
	private Long metamodelPropertyId;

	/**
	* 元数据id
	*/
	private Long metadataId;

	/**
	* 属性值
	*/
	private String property;

	/**
	* 项目id（租户id）
	*/
	private Long projectId;



}
