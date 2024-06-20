package net.srt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@TableName(value = "visualize_info2", autoResultMap = true)
public class VisualizeInfo2Entity {
    /**
     * id
     */
    @JsonIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学号
     */
    @JsonIgnore
    String NetId;

    /**
     * 公必绩点
     */
    float PubCoursePerf;

    /**
     * 专选绩点
     */
    float EleCoursePerf;

    /**
     * 专必绩点
     */
    float MandSpecCoursePerf;

    /**
     * 党政思建获奖
     */
    int JdIdeaPolAwards;

    /**
     * 学科竞赛获奖
     */
    int DiscCompAwards;

    /**
     * 艺术比赛获奖
     */
    int ArtCompAwards;

    /**
     * 体育比赛获奖
     */
    int SportCompAwards;

    /**
     * 实践创业竞赛获奖
     */
    int EntrepCompAwards;

    /**
     * 学术成果获奖
     */
    int AcademicAwards;

    /**
     * 高水平论文发表
     */
    int HighLevelPubs;

    /**
     * 志愿服务时长
     */
    int VolServHours;

    /**
     * 专利发明
     */
    int Patents;

    /**
     * 软件著作
     */
    int SoftCopyInventions;

    /**
     * 专著出版
     */
    int MonographsPub;

    /**
     * 班级号
     */
    @JsonIgnore
    int ClassId;

    /**
     * 年级号
     */
    @JsonIgnore
    int GradeId;
}
