package net.srt.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

/**
 * 数据资产-资源挂载表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-07-06
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("data_assets_resource_mount")
public class DataAssetsResourceMountEntity extends BaseEntity {

	/**
	 * 资源id
	 */
	private Long resourceId;

	/**
	 * 1-数据库表 2-api 3-文件
	 */
	private Integer mountType;

	/**
	 * 挂载的数据id（元数据，api，文件）
	 */
	private Long mountId;

	private String mountName;

	/**
	 * 项目id
	 */
	private Long projectId;

	private Integer deleted;


	private Integer status;
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String checkLog;


}
