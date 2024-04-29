package net.srt.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * 数据服务-api分组
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-01-28
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_service_api_group")
public class DataServiceApiGroupEntity extends BaseEntity {

	/**
	* 父级id（顶级为0）
	*/
	private Long parentId;

	/**
	* 1-文件夹 2-api目录
	*/
	private Integer type;

	/**
	* 名称
	*/
	private String name;

	/**
	* 描述
	*/
	private String description;

	/**
	* 序号
	*/
	private Integer orderNo;

	/**
	* 路径
	*/
	private String path;

	/**
	* 项目id
	*/
	private Long projectId;

	private Long orgId;







}
