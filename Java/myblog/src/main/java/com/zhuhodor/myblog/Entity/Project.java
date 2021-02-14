package com.zhuhodor.myblog.Entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("project")
public class Project implements Serializable {
    private Integer id;

    private Integer startUser;

    private String projectName;

    private Timestamp createAt;

    private Integer rates;

    private String overview;
}
