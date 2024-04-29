package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

/**
 * 数据集市-资源评价表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-08-27
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_market_resource_comment")
public class DataMarketResourceCommentEntity extends BaseEntity {

	/**
	* 资源id
	*/
	private Long resourceId;

	/**
	* 评价
	*/
	private String comment;

	/**
	* 0-0星 1-1星 2-2星 3-3星 4-4星 5-5星
	*/
	private Integer level;

	/**
	* 项目id
	*/
	private Long projectId;







}
