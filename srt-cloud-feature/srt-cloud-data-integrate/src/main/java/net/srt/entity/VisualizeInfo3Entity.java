package net.srt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@TableName(value = "visualize_info3", autoResultMap = true)
public class VisualizeInfo3Entity {
    /**
     * id
     */
    @TableId(value = "StudentID", type = IdType.AUTO)
    private Integer StudentID;

    /**
     * 名字
     */
    String Name;

    /**
     * 学生类别
     */
    String Category;

    /**
     * 性别
     */
    String Gender;

    /**
     * 民族
     */
    String Ethnicity;

    /**
     * 出生日期
     */
    String BirthDate;

    /**
     * 教育程度
     */
    String EducationLevel;

    /**
     * 政治面貌
     */
    String PoliticalStatus;

    /**
     * 籍贯
     */
    String Hometown;

    /**
     * 高考分数
     */
    String HighScore;

    /**
     * 入学年份
     */
    Integer EnterYear;
}
