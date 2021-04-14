package com.zhuhodor.myblog.vo;

import lombok.Data;

import java.util.List;

@Data
public class EntityVo {
    private List<ItemVo> Alias;
    private List<ItemVo> Extra;
    private List<ItemVo> Foundin;
    private List<ItemVo> Id;
    private List<ItemVo> Popular;
    private List<ItemVo> Name;
    private List<ItemVo> Type;
    private List<ItemVo> Types;
    private List<ItemVo> 简介;
}
