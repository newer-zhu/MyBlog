package com.zhuhodor.myblog.service;

import com.zhuhodor.myblog.Entity.BlogModule.Blog_Column;
import org.springframework.stereotype.Service;

import java.lang.ref.SoftReference;
import java.util.List;

@Service
public interface ColumnService {
    boolean createBlogColumn(Blog_Column blog_column);

    boolean blogToColumn(String blog_id, String column_id);

    void delBlogInColumn(String blogId,String columnId);

    List<Blog_Column> getColumnsByUserId(String userId);

    List<Integer> getColumnIdByBlogId(String blogId);

    boolean delColumnById(String column_id);

    Integer count(int column_id);
}
