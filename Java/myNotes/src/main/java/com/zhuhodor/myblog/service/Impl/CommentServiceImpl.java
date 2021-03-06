package com.zhuhodor.myblog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuhodor.myblog.Entity.BlogModule.BlogComment;
import com.zhuhodor.myblog.mapper.CommentMapper;
import com.zhuhodor.myblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, BlogComment> implements CommentService{

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<BlogComment> selectCommentByBlogId(int blogId) {
        return commentMapper.selectCommentByBlogId(blogId);
    }

    @Override
    public List<BlogComment> selectChildrenByCommentId(int comment_id) {
        return commentMapper.selectChildrenByCommentId(comment_id);
    }

    @Override
    public boolean commentToBlog(int commentId, int blogId) {
        return commentMapper.commentToBlog(commentId, blogId);
    }

    @Override
    public boolean childCommentToParent(int commentId, int targetComment) {
        return commentMapper.childCommentToParent(commentId, targetComment);
    }


}
