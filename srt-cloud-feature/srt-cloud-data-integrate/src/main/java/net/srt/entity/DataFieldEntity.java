package net.srt.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import net.srt.framework.mybatis.entity.BaseEntity;

import java.util.Date;
/**
 * 字段表Entity
 *
*/

@EqualsAndHashCode(callSuper=false)
@Data
@TableName("data_field")
public class DataFieldEntity extends BaseEntity{
    /**
     * 是否自动排序
     */
    private Boolean autoIncrement;
    /**
     * 所属数据表id
     */
    private Long datatableId;
    /**
     * 所属数据表名字
     */
    private String datatableName;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 数据长度
     */
    private Long displaySize;
    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 字段类型
     */
    private String fieldTypeName;
    /**
     * 是否为空
     */
    private Boolean nullable;
    /**
     * 是否主键
     */
    private Boolean pk;
    /**
     * 注释
     */
    private String remarks;
    /**
     * 小数位数
     */
    private Long scaleSize;


}
