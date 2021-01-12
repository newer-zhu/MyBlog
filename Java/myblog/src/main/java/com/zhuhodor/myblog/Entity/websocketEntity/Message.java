package com.zhuhodor.myblog.Entity.websocketEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Message {
    private String content;
    private Timestamp time;
    private String Url;
}
