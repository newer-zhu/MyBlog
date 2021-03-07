package com.zhuhodor.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.Entity.Project;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;


public interface ProjectMapper extends BaseMapper<Project> {

    @Select("SELECT b.* from blog b, project_blog pb WHERE b.id = pb.blog_id " +
            "AND pb.project_id = #{projectId} order by createdAt DESC")
    List<Blog> findBlogsByProjectId(String projectId);

    @Insert("INSERT INTO favorite_project(user_id, project_id, time) VALUES(#{userId}, #{projectId}, #{time})")
    boolean favorite(String userId, String projectId, String time);

    @Delete("DELETE FROM favorite_project WHERE user_id = #{userId} AND project_id = #{projectId}")
    boolean cancel(String userId, String projectId);

    @Select("SELECT p.* FROM project p, favorite_project fp WHERE " +
            "fp.project_id = p.id and fp.user_id = #{userId} ORDER BY fp.time DESC")
    List<Project> getFavoriteByUserId(String userId);

    @Select("SELECT count(*) from favorite_project where user_id = #{userId} and project_id = #{projectId}")
    Integer isFavorite(String userId, String projectId);

    @Select("SELECT count(*) from favorite_project where project_id = #{projectId}")
    Integer favoriteCount(String projectId);

    @Select("SELECT p.* FROM project p, project_blog pb WHERE p.id = pb.project_id and pb.blog_id = #{blogId}")
    Project getProjectByBlogId(String blogId);

    @Select("SELECT b.createdAt FROM blog b, project_blog pb WHERE b.id = pb.blog_id AND pb.project_id = #{projectId} order by b.createdAt DESC")
    List<Date> getBlogTime(String projectId);
}
