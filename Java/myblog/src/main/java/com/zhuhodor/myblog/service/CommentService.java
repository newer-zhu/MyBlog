package com.zhuhodor.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhuhodor.myblog.Entity.BlogModule.BlogComment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService extends IService<BlogComment> {
    List<BlogComment> selectCommentByBlogId(int blogId);

    List<BlogComment> selectChildrenByCommentId(int comment_id);

    boolean commentToBlog(int commentId, int blogId);

    boolean childCommentToParent(int commentId, int targetComment);

}
