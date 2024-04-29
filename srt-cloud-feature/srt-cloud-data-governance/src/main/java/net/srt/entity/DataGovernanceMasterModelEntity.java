package net.srt.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import net.srt.framework.mybatis.entity.BaseEntity;
import net.srt.vo.DataGovernanceMasterColumnVO;

import java.util.Date;
import java.util.List;

/**
 * 数据治理-主数据模型
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-09-27
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_governance_master_model")
public class DataGovernanceMasterModelEntity extends BaseEntity {

	/**
	* 主数据目录id
	*/
	private Long catalogId;

	/**
	* 表名
	*/
	private String tableName;

	/**
	* 中文名称
	*/
	private String tableCn;

	/**
	* 备注
	*/
	private String description;

	/**
	* 发布状态 0-未发布 1-已发布
	*/
	private Integer status;

	/**
	* 发布时间
	*/
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Date releaseTime;

	/**
	* 发布人id
	*/
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Long releaseUserId;

	/**
	* 项目id（租户id）
	*/
	private Long projectId;
	private Long orgId;


	@TableField(exist = false)
	private List<DataGovernanceMasterColumnVO> columns;



}
