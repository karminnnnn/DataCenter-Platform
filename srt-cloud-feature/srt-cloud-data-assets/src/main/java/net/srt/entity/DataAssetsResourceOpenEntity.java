package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

/**
 * 数据资产-资产开放关联表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-07-22
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_assets_resource_open")
public class DataAssetsResourceOpenEntity extends BaseEntity {

	/**
	* 所属目录id
	*/
	private Long resourceId;

	/**
	* 2-角色 3-用户
	*/
	private Integer openType;

	/**
	* 角色或用户id
	*/
	private Long openId;

	/**
	* 项目id
	*/
	private Long projectId;

	private Integer deleted;





}
