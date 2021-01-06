package com.zhuhodor.myblog.service.Impl;

import com.zhuhodor.myblog.Entity.Blog;
import com.zhuhodor.myblog.Entity.Blog_Column;
import com.zhuhodor.myblog.mapper.BlogMapper;
import com.zhuhodor.myblog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Component
public class BlogServiceImpl implements BlogService {

    @Autowired
    BlogMapper blogMapper;

    @Override
    public List<Blog> findBlogsByUserId(String userId) {
        return blogMapper.findBlogsByUserId(userId);
    }

    @Override
    public int getPagesByUserId(String userId) {
        return blogMapper.getPagesByUserId(userId);
    }

    @Override
    public boolean delBlog(String blogId) {
        return blogMapper.delBlog(blogId);
    }

    @Override
    public boolean editBlog(Blog blog) {
        return blogMapper.editBlog(blog);
    }

    @Override
    public boolean saveBlog(Blog blog) {
        return blogMapper.saveBlog(blog);
    }

    @Override
    public Blog findBlogById(String id) {
        return blogMapper.findBlogById(id);
    }

    @Override
    public boolean createBlogColumn(Blog_Column blog_column) {
        return blogMapper.createBlogColumn(blog_column);
    }

    @Override
    public boolean blogToColumn(String blog_id, String column_id) {
        return blogMapper.blogToColumn(blog_id, column_id);
    }

    @Override
    public List<Blog_Column> getColumnsByUserId(String userId) {
        return blogMapper.getColumnsByUserId(userId);
    }
}
