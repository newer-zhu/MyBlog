package com.zhuhodor.myblog.service.Impl;

import com.zhuhodor.myblog.Entity.Blog_Column;
import com.zhuhodor.myblog.mapper.ColumnMapper;
import com.zhuhodor.myblog.service.ColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class ColumnServiceImpl implements ColumnService {

    @Autowired
    ColumnMapper columnMapper;

    @Override
    public boolean createBlogColumn(Blog_Column blog_column) {
        return columnMapper.createBlogColumn(blog_column);
    }

    @Override
    public boolean blogToColumn(String blog_id, String column_id) {
        return columnMapper.blogToColumn(blog_id, column_id);
    }



    @Override
    public List<Blog_Column> getColumnsByUserId(String userId) {
        return columnMapper.getColumnsByUserId(userId);
    }
}
