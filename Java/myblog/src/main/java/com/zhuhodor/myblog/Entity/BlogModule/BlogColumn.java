package com.zhuhodor.myblog.Entity.BlogModule;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)//setter方法返回当前对象
@EqualsAndHashCode(callSuper = false)
/**
 * 博客分栏
 */
public class BlogColumn {
    private Integer id;
    private int userId;
    private String cName;

    private int count;
}
