package com.zhuhodor.myblog.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("rel_comment")
public class Rel_CommentVo {
    private int comment_id;
    private int targetUser;
    private int targetBlog;

}
