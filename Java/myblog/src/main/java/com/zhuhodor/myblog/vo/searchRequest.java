package com.zhuhodor.myblog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class searchRequest {
    //搜索框内容
    private String querystr;
    //时间范围
    private String start;
    private String end;
    //仅自己
    private String userId;
    //分页
    private Integer current;
}
