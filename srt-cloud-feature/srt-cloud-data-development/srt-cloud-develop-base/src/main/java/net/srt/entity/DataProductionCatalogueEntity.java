package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

/**
 * 数据生产目录
 *
 * @author zrx 985134801@qq.com
 * @since 2.0.0 2022-11-27
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName(value = "data_production_catalogue", autoResultMap = true)
public class DataProductionCatalogueEntity extends BaseEntity {

	/**
	 * 项目（租户）id
	 */
	private Long projectId;
	private Long orgId;

	/**
	 * 父级id
	 */
	private Long parentId;

	/**
	 * 节点路径
	 */
	private String path;

	/**
	 * 关联的作业id
	 */
	private Long taskId;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 数据生产-作业类型（字典）
	 */
	private Integer taskType;
	private Integer taskSqlType;


	/**
	 * 是否可用（0-否 1-是）
	 */
	private Integer enabled;

	/**
	 * 是否叶子节点
	 */
	private Integer ifLeaf;

	/**
	 * 序号
	 */
	private Long orderNo;

	/**
	 * 描述
	 */
	private String description;


}
