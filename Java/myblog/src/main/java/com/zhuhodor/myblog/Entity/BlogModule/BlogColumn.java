package com.zhuhodor.myblog.Entity.BlogModule;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)//setter方法返回当前对象
@EqualsAndHashCode(callSuper = false)
@TableName("blog_column")
/**
 * 博客分栏
 */
public class BlogColumn {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private int userId;
    private String cName;

    private int count;
}
