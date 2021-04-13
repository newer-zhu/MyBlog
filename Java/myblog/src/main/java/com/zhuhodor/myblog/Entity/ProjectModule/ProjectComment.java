package com.zhuhodor.myblog.Entity.ProjectModule;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhuhodor.myblog.Entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("project_comment")
public class ProjectComment {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer projectId;
    private String comment;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;
    private Integer rates;
    private Integer userId;

    @TableField(exist = false)
    private User user;
}
