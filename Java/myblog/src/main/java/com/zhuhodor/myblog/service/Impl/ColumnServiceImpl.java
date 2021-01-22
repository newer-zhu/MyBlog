package com.zhuhodor.myblog.service.Impl;

import com.zhuhodor.myblog.Entity.BlogModule.Blog_Column;
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
    public void delBlogInColumn(String blogId,String columnId) {
        columnMapper.delBlogInColumn(blogId, columnId);
    }


    @Override
    public List<Blog_Column> getColumnsByUserId(String userId) {
        return columnMapper.getColumnsByUserId(userId);
    }

    @Override
    public List<Integer> getColumnIdByBlogId(String blogId) {
        return columnMapper.getColumnIdByBlogId(blogId);
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
