package com.zhuhodor.myblog.mapper;

import com.zhuhodor.myblog.Entity.Blog;
import com.zhuhodor.myblog.Entity.Blog_Column;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BlogMapper {
    @Select("SELECT * FROM blog WHERE userId = #{userId} order by createdAt DESC")
    public List<Blog> findBlogsByUserId(String userId);

    @Delete("DELETE FROM blog WHERE id = #{blogId}")
    public boolean delBlog(String blogId);

    @Insert("INSERT INTO blog (userId, title, summary, content, createdAt) VALUES(#{userId},#{title},#{summary},#{content},#{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public boolean saveBlog(Blog blog);

    @Update("UPDATE blog SET title = #{title}, summary = #{summary}, content = #{content}, createdAt = #{createdAt} WHERE id = #{id}")
    public boolean editBlog(Blog blog);

    @Select("Select * from blog where id = #{id}")
    public Blog findBlogById(String id);

    @Select("SELECT count(*) FROM blog where userId = #{userId}")
    public int getPagesByUserId(String userId);

    @Insert("INSERT INTO blog_column(c_name, user_id) VALUES (#{c_name}, #{user_id})")
    public boolean createBlogColumn(Blog_Column blog_column);

    @Insert("INSERT INTO blog_with_column(blog_id, column_id) VALUES(#{blog_id}, #{column_id})")
    public boolean blogToColumn(String blog_id, String column_id);

    @Select("SELECT * from blog_column where user_id = #{userId}")
    public List<Blog_Column> getColumnsByUserId(String userId);


}
