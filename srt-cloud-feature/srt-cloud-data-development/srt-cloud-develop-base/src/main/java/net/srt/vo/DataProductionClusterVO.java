package net.srt.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.srt.framework.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
* 数据生产-集群实例
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2022-12-01
*/
@Data
@Schema(description = "数据生产-集群实例")
public class DataProductionClusterVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键id")
	private Long id;

	@Schema(description = "项目（租户id）")
	private Long projectId;
	private Long orgId;

	@Schema(description = "实例名称")
	private String name;

	@Schema(description = "别名")
	private String alias;

	@Schema(description = "实例类型 1-Standalone 2-Yarn Session 3-Yarn Per-Job 4-Yarn Application 5-K8s Session 6-K8s Application")
	private Integer type;

	@Schema(description = "配置的实例集群地址")
	private String hosts;

	@Schema(description = "Job Manager 地址")
	private String jobManagerHost;

	@Schema(description = "flink版本")
	private String flinkVersion;

	@Schema(description = "集群状态")
	private Integer status;

	@Schema(description = "备注")
	private String note;

	@Schema(description = "是否是自动注册")
	private Boolean autoRegisters;

	@Schema(description = "关联的集群配置地址id（如果是 yarn per-job 或 yarn/k8s session/apllication 会存在此id）")
	private Integer clusterConfigurationId;

	@Schema(description = "任务id")
	private Long taskId;

	@Schema(description = "是否可用")
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


}
