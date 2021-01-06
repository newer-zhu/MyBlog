package com.zhuhodor.myblog.Entity;

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
    int id;
    int user_id;
    String c_name;
}
