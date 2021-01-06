package com.zhuhodor.myblog.service;

import com.zhuhodor.myblog.Entity.Blog;
import com.zhuhodor.myblog.Entity.Blog_Column;

import java.util.List;

public interface BlogService {
    public List<Blog> findBlogsByUserId(String userId);

    public int getPagesByUserId(String userId);

    public boolean delBlog(String blogId);

    public boolean editBlog(Blog blog);

    public boolean saveBlog(Blog blog);

    public Blog findBlogById(String id);

    public boolean createBlogColumn(Blog_Column blog_column);

    public boolean blogToColumn(String blog_id, String column_id);

    public List<Blog_Column> getColumnsByUserId(String userId);
}
