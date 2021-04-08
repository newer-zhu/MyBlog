package com.zhuhodor.myblog.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.dynalink.linker.LinkerServices;
import lombok.Data;

import java.util.List;

/**
 * 专业选择框
 */
@Data
public class MajorOpVo {
    @JsonProperty("value")
    private int id;
    @JsonProperty("label")
    private String name;
    private int popular;

    private List<MajorOpVo> children;
}
