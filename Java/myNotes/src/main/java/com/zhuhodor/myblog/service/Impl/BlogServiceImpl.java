package com.zhuhodor.myblog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.mapper.BlogMapper;
import com.zhuhodor.myblog.service.BlogService;
import com.zhuhodor.myblog.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

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
    public boolean saveBlog(Blog blog, String projectId) {
        boolean b = blogMapper.saveBlog(blog);
        if (projectId != "" && !"undefined".equals(projectId)){
            blogMapper.blogToProject(blog.getId(), projectId);
        }
        return b;
    }

    @Override
    public Blog findBlogById(String id) {
        return blogMapper.findBlogById(id);
    }

    @Override
    public List<Blog> getPagesByColumnId(String columnId) {
        return blogMapper.getPagesByColumnId(columnId);
    }


}
