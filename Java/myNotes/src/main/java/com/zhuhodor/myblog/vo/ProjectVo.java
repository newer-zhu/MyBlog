package com.zhuhodor.myblog.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProjectVo {
    private Integer id;

    private Integer startUser;

    private String projectName;

    private List<OverviewVo> overview;
}
