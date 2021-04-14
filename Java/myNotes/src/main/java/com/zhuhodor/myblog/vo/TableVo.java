package com.zhuhodor.myblog.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class TableVo {

    private Integer userId;

    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8",shape = JsonFormat.Shape.STRING)
    Date date;

    Integer number;
}
