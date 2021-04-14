package com.zhuhodor.myblog.Entity.ProjectModule;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhuhodor.myblog.Entity.User;
import com.zhuhodor.myblog.vo.PictureVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("project")
public class Project implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer startUser;

    private String projectName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createAt;

    private Float rates;

    private String overview;

    @TableField(exist = false)
    private User projectUser;

}
