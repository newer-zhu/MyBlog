package com.zhuhodor.myblog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.Entity.Comment;
import com.zhuhodor.myblog.Entity.Tag;
import com.zhuhodor.myblog.mapper.CommentMapper;
import com.zhuhodor.myblog.mapper.TagMapper;
import com.zhuhodor.myblog.service.CommentService;
import com.zhuhodor.myblog.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@Component
@Slf4j
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Autowired
    TagMapper tagMapper;


    @Override
    public List<Tag> getTagsbyBlogId(String blogId) {
        return tagMapper.getTagsbyBlogId(blogId);
    }

    @Override
    public boolean removeBlogsInTag(String blogId) {
        return tagMapper.removeBlogsInTag(blogId);
    }

    @Override
    public boolean blogIntoTags(String blogId, String tagId) {
        return tagMapper.blogIntoTags(blogId, tagId);
    }

    @Override
    public List<Blog> findBlogsByTagId(String tagId) {
        return tagMapper.findBlogsByTagId(tagId);
    }
}
