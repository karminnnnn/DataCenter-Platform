package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

/**
 * 数据治理-元数据标准关联表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-23
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("data_governance_metadata_standard_rel")
public class DataGovernanceMetadataStandardRelEntity extends BaseEntity {

	/**
	 * 元数据id
	 */
	private Long metadataId;

	/**
	 * 标准字段id
	 */
	private Long standardId;

	/**
	 * 真删除
	 */
	private Integer deleted;


}
