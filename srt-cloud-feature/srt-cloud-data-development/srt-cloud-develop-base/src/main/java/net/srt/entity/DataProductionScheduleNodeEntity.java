package net.srt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.Objects;

/**
 * 数据生产-作业调度节点
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-01-12
 */
@Data
@TableName("data_production_schedule_node")
public class DataProductionScheduleNodeEntity extends BaseEntity {

	private Long projectId;
	private Long orgId;
	/**
	* 关联的调度id
	*/
	private Long taskScheduleId;

	/**
	* 节点编号
	*/
	private String no;

	/**
	* 执行顺序
	*/
	private Integer sort;

	/**
	* 节点名称
	*/
	private String name;

	/**
	 * 节点类型
	 */
	private String type;

	/**
	 * 横坐标
	 */
	private Integer x;

	/**
	 * 纵坐标
	 */
	private Integer y;

	/**
	* 节点描述
	*/
	private String note;

	/**
	* 关联的作业id
	*/
	private Integer taskId;

	/**
	 * 作业类型
	 */
	private Integer taskType;

	/**
	 * 遇错是否继续
	 */
	private Integer failGoOn;

	/**
	 * 权重
	 */
	private Integer weight;


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		DataProductionScheduleNodeEntity that = (DataProductionScheduleNodeEntity) o;
		return Objects.equals(no, that.no);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), no);
	}
}
