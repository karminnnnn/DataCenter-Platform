package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

/**
 * 数据治理-元数据
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-03-29
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_governance_metadata")
public class DataGovernanceMetadataEntity extends BaseEntity {

	/**
	* 父级id（默认0为顶级）
	*/
	private Long parentId;

	/**
	* 树状节点的路径
	*/
	private String path;

	/**
	* 节点名称
	*/
	private String name;

	/**
	* 节点英文名称
	*/
	private String code;

	private Integer ifLeaf;
	/**
	* 对应的元模型id
	*/
	private Long metamodelId;

	private String icon;

	/**
	* 详情
	*/
	private String description;

	/**
	* 数据库类型（1-数据库 2-中台库）
	*/
	private Integer dbType;

	/**
	* 如果是外部系统接入的库表，需要此字段
	*/
	private Long datasourceId;

	/**
	* 采集任务id
	*/
	private Long collectTaskId;

	/**
	* 项目id（租户id）
	*/
	private Long projectId;
	private Long orgId;


	private Integer orderNo;






}
