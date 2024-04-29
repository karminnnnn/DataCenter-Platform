package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

/**
 * 数据治理-数据标准
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-05
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("data_governance_standard")
public class DataGovernanceStandardEntity extends BaseEntity {

	/**
	 * 所属目录id
	 */
	private Long categoryId;

	/**
	 * 标准英文名称
	 */
	private String engName;

	/**
	 * 标准中文名称
	 */
	private String cnName;

	/**
	 * 编码数
	 */
	private Integer codeNum;

	/**
	 * 数据类型 数字，字符串，日期，小数
	 */
	private String dataType;

	/**
	 * 长度
	 */
	private Integer dataLength;

	/**
	 * 精度
	 */
	private Integer dataPrecision;

	/**
	 * 非空 0-否 1-是
	 */
	private Integer nullable;

	/**
	 * 标准码表id
	 */
	private Integer standardCodeId;

	/**
	 * 1-标准字段 2-标准码表
	 */
	private Integer type;

	/**
	 * 描述
	 */
	private String note;

	/**
	 * 项目（租户）id
	 */
	private Long projectId;

	private Integer status;


}
