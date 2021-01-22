package com.zhuhodor.myblog.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ColumnList {
    private String blogId;
    private List<String> before;
    private List<String> after;
}
