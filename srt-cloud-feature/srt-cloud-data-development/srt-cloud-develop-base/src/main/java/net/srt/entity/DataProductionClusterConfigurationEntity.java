package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.flink.common.assertion.Asserts;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据生产-集群配置
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-20
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("data_production_cluster_configuration")
public class DataProductionClusterConfigurationEntity extends BaseEntity {

	/**
	 * 项目（租户）id
	 */
	private Long projectId;
	private Long orgId;

	/**
	 * cluster configuration name
	 */
	private String name;

	/**
	 * cluster configuration alias
	 */
	private String alias;

	/**
	 * cluster type
	 */
	private String type;

	/**
	 * json of configuration
	 */
	private String configJson;

	/**
	 * is available
	 */
	private Boolean isAvailable;

	/**
	 * note
	 */
	private String note;

	/**
	 * is enable
	 */
	private Boolean enabled;

	@TableField(exist = false)
	private Map config = new HashMap<>();


	public Map<String, Object> parseConfig() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			if (Asserts.isNotNullString(configJson)) {
				config = objectMapper.readValue(configJson, Map.class);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return config;
	}
}
