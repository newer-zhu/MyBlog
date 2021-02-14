package com.zhuhodor.myblog.service.Impl;

import com.zhuhodor.myblog.Entity.BlogModule.BlogColumn;
import com.zhuhodor.myblog.mapper.ColumnMapper;
import com.zhuhodor.myblog.service.ColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class ColumnServiceImpl implements ColumnService {

    @Autowired
    ColumnMapper columnMapper;

    @Override
    public boolean createBlogColumn(BlogColumn blog_column) {
        return columnMapper.createBlogColumn(blog_column);
    }

    @Override
    public boolean blogToColumn(String blog_id, String column_id) {
        return columnMapper.blogToColumn(blog_id, column_id);
    }

    @Override
    public void delBlogInColumn(String blogId) {
        columnMapper.delBlogInColumn(blogId);
    }


    @Override
    public List<BlogColumn> getColumnsByUserId(String userId) {
        return columnMapper.getColumnsByUserId(userId);
    }

    @Override
    public BlogColumn getColumnByBlogId(String blogId) {
        return columnMapper.getColumnByBlogId(blogId);
    }

    @Override
    public boolean delColumnById(String column_id) {
        return columnMapper.delColumnById(column_id);
    }

    @Override
    public Integer count(int column_id) {
        return columnMapper.count(column_id);
    }
}
