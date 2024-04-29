package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

/**
 * 数据生产-jar管理
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-11-13
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_production_jar")
public class DataProductionJarEntity extends BaseEntity {

	/**
	* 项目（租户id）
	*/
	private Long projectId;

	private Long orgId;

	/**
	* 名称
	*/
	private String name;

	private Integer submitType;
	/**
	* 文件地址
	*/
	private String path;

	/**
	* 主类
	*/
	private String mainClass;

	/**
	* 参数
	*/
	private String params;

	/**
	* 备注
	*/
	private String note;







}
