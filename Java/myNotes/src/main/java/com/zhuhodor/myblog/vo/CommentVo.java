package com.zhuhodor.myblog.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CommentVo {
    private int id;
    private CommentUser commentUser;
    private CommentUser targetUser;
    private String content;
    private String createDate;
    private List<CommentVo> childrenList;
}
