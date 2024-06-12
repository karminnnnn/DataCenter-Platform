package net.srt.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_user_activity_log")
public class SysUserActivityLogEntity {
    /**
     * id
     */
    @TableId
    Integer id;

    /**
     * 用户名
     */
    String username;

    /**
     * 活动类型
     */
    Integer action;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
