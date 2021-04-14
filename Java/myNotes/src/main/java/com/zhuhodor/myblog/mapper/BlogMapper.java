package com.zhuhodor.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BlogMapper extends BaseMapper<Blog> {
    @Select("SELECT * FROM blog WHERE user_id = #{userId} order by created_at DESC")
    List<Blog> findBlogsByUserId(String userId);

    @Delete("DELETE FROM blog WHERE id = #{blogId}")
    boolean delBlog(String blogId);

    @Insert("INSERT INTO blog (user_id, title, summary, content, created_at, is_file, visitors)" +
            " VALUES(#{userId},#{title},#{summary},#{content},#{createdAt}, #{isFile}, #{visitors})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    boolean saveBlog(Blog blog);

    @Update("UPDATE blog SET title = #{title}, summary = #{summary}, content = #{content}, " +
            "created_at = #{createdAt}, is_file = #{isFile}, visitors = #{visitors} WHERE id = #{id}")
    boolean editBlog(Blog blog);

    @Select("Select * from blog where id = #{id}")
    Blog findBlogById(String id);

    @Select("SELECT count(*) FROM blog where user_id = #{userId}")
    int getPagesByUserId(String userId);

    @Select("SELECT b.* FROM blog b, blog_with_column bwc WHERE b.id = bwc.blog_id AND bwc.column_id = #{columnId} order by created_at DESC")
    List<Blog> getPagesByColumnId(String columnId);

    @Insert("INSERT INTO project_blog(project_id, blog_id) VALUES(#{projectId}, #{blogId})")
    boolean blogToProject(String blogId, String projectId);



}
