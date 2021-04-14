package com.zhuhodor.myblog.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RequestVo {
    private Integer id;

    private Integer projectId;

    private Integer contributorId;

    private String username;

    private String projectName;

    public RequestVo(Integer projectId, Integer contributorId, String username, String projectName) {
        this.projectId = projectId;
        this.contributorId = contributorId;
        this.username = username;
        this.projectName = projectName;
    }
}
