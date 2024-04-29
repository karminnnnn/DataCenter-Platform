package net.srt.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.srt.flink.common.assertion.Asserts;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据生产-集群配置
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-20
 */
@Data
@Schema(description = "数据生产-集群配置")
public class DataProductionClusterConfigurationVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "ID")
	private Long id;

	@Schema(description = "项目（租户）id")
	private Long projectId;
	private Long orgId;

	@Schema(description = "cluster configuration name")
	private String name;

	@Schema(description = "cluster configuration alias")
	private String alias;

	@Schema(description = "cluster type")
	private String type;

	@Schema(description = "json of configuration")
	private String configJson;

	@Schema(description = "is available")
	private Boolean isAvailable;

	@Schema(description = "note")
	private String note;

	@Schema(description = "is enable")
	private Boolean enabled;

	@Schema(description = "版本号")
	private Integer version;

	@Schema(description = "删除标识  0：正常   1：已删除")
	private Integer deleted;

	@Schema(description = "创建者")
	private Long creator;

	@Schema(description = "创建时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date createTime;

	@Schema(description = "更新者")
	private Long updater;

	@Schema(description = "更新时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date updateTime;

	@TableField(exist = false)
	private Map<String, Object> config = new HashMap<>();


	public Map<String, Object> parseConfig() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			if (Asserts.isNotNullString(configJson)) {
				config = objectMapper.readValue(configJson, HashMap.class);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return config;
	}
}
