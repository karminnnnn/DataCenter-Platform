package net.srt.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.flink.common.assertion.Asserts;
import net.srt.flink.core.job.JobConfig;
import net.srt.flink.gateway.GatewayType;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据生产任务 与 DataProductionTaskHistoryEntity 一对多
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-05
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName(value = "data_production_task", autoResultMap = true)
public class DataProductionTaskEntity extends BaseEntity {


	/**
	 * 节点id
	 */
	private Long catalogueId;

	/**
	 * 任务名称
	 */
	private String name;

	/**
	 * 项目（租户id）
	 */
	private Long projectId;
	private Long orgId;

	/**
	 * 任务别名
	 */
	private String alias;

	/**
	 * 任务类型
	 */
	private Integer dialect;

	/**
	 * 任务运行类型
	 */
	private Integer type;

	/**
	 * CheckPoint trigger seconds
	 */
	private Integer checkPoint;

	/**
	 * SavePoint strategy
	 */
	private Integer savePointStrategy;

	/**
	 * SavePointPath
	 */
	private String savePointPath;

	/**
	 * 并行度
	 */
	private Integer parallelism;

	/**
	 * 全局变量
	 */
	private Boolean fragment;

	/**
	 * insert 语句集
	 */
	private Boolean statementSet;

	/**
	 * 批处理模式
	 */
	private Boolean batchModel;

	/**
	 * flink集群实例id
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Long clusterId;

	/**
	 * 集群配置id
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Long clusterConfigurationId;

	/**
	 * 数据类型（1-数据库 2-中台库）（sql模式下）
	 */
	private Integer sqlDbType;

	/**
	 * 数据库id（sql模式下）
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Long databaseId;

	private Integer openTrans;

	/**
	 * Jar ID
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Long jarId;

	/**
	 * env id
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Long envId;

	/**
	 * alert group id
	 */
	private Long alertGroupId;

	/**
	 * configuration json
	 */
	private String configJson;

	/**
	 * Job Note
	 */
	private String note;

	/**
	 * Job lifecycle
	 */
	private Integer step;

	/**
	 * job instance id
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Long jobInstanceId;

	/**
	 * 自动停止
	 */
	private Boolean useAutoCancel;

	/**
	 * 打印流
	 */
	private Boolean useChangeLog;

	/**
	 * 预览结果
	 */
	private Boolean useResult;

	/**
	 * 预览行数
	 */
	private Integer pvdataNum;

	/**
	 * is enable
	 */
	private Boolean enabled;

	/**
	 * version id
	 */
	private Integer versionId;
	private String flinkVersion;

	@TableField(exist = false)
	private String statement;

	@TableField(exist = false)
	private String clusterName;

	@TableField(exist = false)
	private List<Map<String, String>> config = new ArrayList<>();


	public List<Map<String, String>> parseConfig() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			if (Asserts.isNotNullString(configJson)) {
				config = objectMapper.readValue(configJson, ArrayList.class);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return config;
	}

	public JobConfig buildSubmitConfig() {
		boolean useRemote = true;
		if (clusterId == null || clusterId == 0) {
			useRemote = false;
		}
		Map<String, String> map = new HashMap<>();
		for (Map<String, String> item : config) {
			if (Asserts.isNotNull(item)) {
				map.put(item.get("key"), item.get("value"));
			}
		}
		int jid = Asserts.isNull(jarId) ? 0 : jarId.intValue();
		boolean fg = Asserts.isNull(fragment) ? false : fragment;
		boolean sts = Asserts.isNull(statementSet) ? false : statementSet;
		return new JobConfig(GatewayType.getByCode(type.toString()).getLongValue(), step, false, false, useRemote, clusterId == null ? null : clusterId.intValue(), clusterConfigurationId == null ? null : clusterConfigurationId.intValue(), jid, getId().intValue(),
				alias, fg, sts, batchModel, checkPoint, parallelism, savePointStrategy, savePointPath, map);
	}
}
