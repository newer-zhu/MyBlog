package com.zhuhodor.myblog.mapper;

import com.zhuhodor.myblog.Entity.BlogModule.Blog;
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




}
