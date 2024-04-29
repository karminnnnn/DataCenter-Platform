package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

/**
 * 数据治理-标准码表数据
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-19
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_governance_standard_code")
public class DataGovernanceStandardCodeEntity extends BaseEntity {

	/**
	* 标准码表id
	*/
	private Long standardId;

	/**
	* 码表数据的id
	*/
	private String dataId;

	/**
	* 码表数据的name
	*/
	private String dataName;


	private Long projectId;
}
