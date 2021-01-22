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
public class Blog_Column {
    private Integer id;
    private int user_id;
    private String c_name;

    private int count;
}
