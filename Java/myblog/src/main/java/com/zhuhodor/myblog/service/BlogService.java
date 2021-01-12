package com.zhuhodor.myblog.service;

import com.zhuhodor.myblog.Entity.Blog;
import com.zhuhodor.myblog.Entity.Blog_Column;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface BlogService {
    public List<Blog> findBlogsByUserId(String userId);

    public int getPagesByUserId(String userId);

    public boolean delBlog(String blogId);

    public boolean editBlog(Blog blog);

    public boolean saveBlog(Blog blog);

    public Blog findBlogById(String id);


}
