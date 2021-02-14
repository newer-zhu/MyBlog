package com.zhuhodor.myblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@CacheConfig(cacheNames = "blog")
public interface BlogService {
    List<Blog> findBlogsByUserId(String userId);

    int getPagesByUserId(String userId);

    @CacheEvict(key = "'id'+#p0")
    boolean delBlog(String blogId);

    @CacheEvict(allEntries = true)
    boolean editBlog(Blog blog);

    boolean saveBlog(Blog blog);

    @Cacheable(key = "'id'+#p0")
    Blog findBlogById(String id);

    List<Blog> getPagesByColumnId(String columnId);

}
