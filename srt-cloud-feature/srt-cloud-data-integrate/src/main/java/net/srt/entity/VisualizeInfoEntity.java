package net.srt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.srt.framework.mybatis.entity.BaseEntity;

@EqualsAndHashCode(callSuper = false)
@Data
@TableName(value = "visualize_info", autoResultMap = true)
public class VisualizeInfoEntity  {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 大类
     */
    int Kind;

    /**
     * 小类
     */
    String Classs;

    /**
     * 教育程度
     */
    String Education;

    /**
     * 年份
     */
    int Year;

    /**
     * 值
     */
    float value;


}
