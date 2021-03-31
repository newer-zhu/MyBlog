package com.zhuhodor.myblog.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("project_pic")
public class PictureVo {
    @TableField("project_id")
    private String projectId;

    @TableField("name")
    private String name;

    @TableField("url")
    private String url;
}
