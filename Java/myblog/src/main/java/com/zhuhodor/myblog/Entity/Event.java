package com.zhuhodor.myblog.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Event {
    private String content;
    private LocalDateTime time;
    private String Url;
    //事件级别
    private Integer level;
}
