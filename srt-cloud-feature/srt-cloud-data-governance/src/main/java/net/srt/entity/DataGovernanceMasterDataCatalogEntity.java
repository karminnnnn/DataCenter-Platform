package net.srt.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * 数据治理-主数据目录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-09-27
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("data_governance_master_data_catalog")
public class DataGovernanceMasterDataCatalogEntity extends BaseEntity {

	/**
	 * 父级id
	 */
	private Long parentId;

	/**
	 * 类型-0-目录 1-主数据
	 */
	private Integer type;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 序号
	 */
	private Integer orderNo;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 目录全路径
	 */
	private String path;

	/**
	 * 项目id
	 */
	private Long projectId;
	private Long orgId;


}
