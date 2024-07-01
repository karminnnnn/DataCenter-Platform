package net.srt.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_log_metadata_activity")
public class SysLogMetadataActiveEntity {
    @TableId
    private Long id;
    private String log;
    private String time;
    @TableField("userName")
    private String userName;
    @TableField(fill = FieldFill.INSERT,value = "createTime")
    private Date createTime;
}
