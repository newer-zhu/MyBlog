package com.zhuhodor.myblog.service;

import com.zhuhodor.myblog.Entity.BlogModule.BlogColumn;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "column")
public interface ColumnService {
    boolean createBlogColumn(BlogColumn blog_column);

    boolean blogToColumn(String blog_id, String column_id);

    void delBlogInColumn(String blogId);

    List<BlogColumn> getColumnsByUserId(String userId);

    BlogColumn getColumnByBlogId(String blogId);

    boolean delColumnById(String column_id);

    Integer count(int column_id);
}
