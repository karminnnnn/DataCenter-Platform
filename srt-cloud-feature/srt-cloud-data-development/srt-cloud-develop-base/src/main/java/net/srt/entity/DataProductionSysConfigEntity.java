package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

/**
 * 数据生产-配置中心
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-26
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_production_sys_config")
public class DataProductionSysConfigEntity extends BaseEntity {

	/**
	* 配置名称
	*/
	private String name;

	/**
	* 配置值
	*/
	private String value;

	private Long projectId;







}
