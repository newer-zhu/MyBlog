package com.zhuhodor.myblog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuhodor.myblog.Entity.Comment;
import com.zhuhodor.myblog.mapper.CommentMapper;
import com.zhuhodor.myblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService{

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> selectCommentByBlogId(int blogId) {
        return commentMapper.selectCommentByBlogId(blogId);
    }

    @Override
    public List<Comment> selectChildrenByCommentId(int comment_id) {
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
