package net.srt.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * 数据治理-标签类型
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-12-24
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_governance_label_type")
public class DataGovernanceLabelTypeEntity extends BaseEntity {

	/**
	* 名称
	*/
	private String name;

	/**
	* 说明
	*/
	private String description;

	private Long projectId;
	private Long orgId;





}
