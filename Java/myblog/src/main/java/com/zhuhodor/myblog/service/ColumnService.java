package com.zhuhodor.myblog.service;

import com.zhuhodor.myblog.Entity.Blog_Column;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ColumnService {
    public boolean createBlogColumn(Blog_Column blog_column);

    public boolean blogToColumn(String blog_id, String column_id);

    public List<Blog_Column> getColumnsByUserId(String userId);
}
