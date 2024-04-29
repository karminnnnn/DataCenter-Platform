package net.srt.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * 数据资产-资产目录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-07-04
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_assets_catalog")
public class DataAssetsCatalogEntity extends BaseEntity {

	/**
	* 父级id
	*/
	private Long parentId;

	/**
	* 类型-0-文件夹 1-资产目录
	*/
	private Integer type;

	/**
	* 目录名称
	*/
	private String name;

	/**
	* 类目代码
	*/
	private String code;

	/**
	* 序号
	*/
	private Integer orderNo;

	/**
	* 描述
	*/
	private String description;

	/**
	* 目录全路径
	*/
	private String path;

	/**
	* 项目id
	*/
	private Long projectId;
	private Long orgId;







}
