package com.zhuhodor.myblog.mapper;

import com.zhuhodor.myblog.Entity.Blog_Column;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ColumnMapper {
    @Insert("INSERT INTO blog_column(c_name, user_id) VALUES (#{c_name}, #{user_id})")
    public boolean createBlogColumn(Blog_Column blog_column);

    @Insert("INSERT INTO blog_with_column(blog_id, column_id) VALUES(#{blog_id}, #{column_id})")
    public boolean blogToColumn(String blog_id, String column_id);

    @Select("SELECT * from blog_column where user_id = #{userId}")
    public List<Blog_Column> getColumnsByUserId(String userId);
}
