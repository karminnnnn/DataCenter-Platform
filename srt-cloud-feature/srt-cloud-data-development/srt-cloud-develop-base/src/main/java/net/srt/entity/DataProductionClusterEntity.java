package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

/**
 * 数据生产-集群实例
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-01
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_production_cluster")
public class DataProductionClusterEntity extends BaseEntity {

	/**
	* 项目（租户id）
	*/
	private Long projectId;
	private Long orgId;


	/**
	* 实例名称
	*/
	private String name;

	/**
	* 别名
	*/
	private String alias;

	/**
	* 实例类型 1-Standalone 2-Yarn Session 3-Yarn Per-Job 4-Yarn Application 5-K8s Session 6-K8s Application
	*/
	private Integer type;

	/**
	* 配置的实例集群地址
	*/
	private String hosts;

	/**
	* Job Manager 地址
	*/
	private String jobManagerHost;

	/**
	* flink版本
	*/
	private String flinkVersion;

	/**
	* 集群状态
	*/
	private Integer status;

	/**
	* 备注
	*/
	private String note;

	/**
	* 是否是自动注册
	*/
	private Boolean autoRegisters;

	/**
	* 关联的集群配置地址id（如果是 yarn per-job 或 yarn/k8s session/apllication 会存在此id）
	*/
	private Integer clusterConfigurationId;

	/**
	* 任务id
	*/
	private Long taskId;

	/**
	* 是否可用
	*/
	private Boolean enabled;

	public static DataProductionClusterEntity autoRegistersCluster(String hosts, String name, String alias, Integer type, Integer clusterConfigurationId, Integer taskId) {
		DataProductionClusterEntity cluster = new DataProductionClusterEntity();
		cluster.setName(name);
		cluster.setAlias(alias);
		cluster.setHosts(hosts);
		cluster.setType(type);
		cluster.setClusterConfigurationId(clusterConfigurationId);
		cluster.setTaskId(taskId.longValue());
		cluster.setAutoRegisters(true);
		cluster.setEnabled(true);
		return cluster;
	}

}
