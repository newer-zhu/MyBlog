package com.zhuhodor.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhuhodor.myblog.Entity.BlogModule.BlogColumn;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "column")
public interface ColumnService extends IService<BlogColumn> {
    boolean createBlogColumn(BlogColumn blog_column);

    boolean blogToColumn(String blog_id, String column_id);

    void delBlogInColumn(String blogId, String columnId);

    List<BlogColumn> getColumnsByUserId(String userId);

    BlogColumn getColumnByBlogId(String blogId, String userId);

    boolean delColumnById(String column_id);

    Integer count(int column_id);
}
