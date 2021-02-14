package com.zhuhodor.myblog.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhuhodor.myblog.Entity.Comment;
import com.zhuhodor.myblog.Entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CommentDao {
    private int id;
    private CommentUser commentUser;
    private CommentUser targetUser;
    private String content;
    private String createDate;
    private List<CommentDao> childrenList;
}
