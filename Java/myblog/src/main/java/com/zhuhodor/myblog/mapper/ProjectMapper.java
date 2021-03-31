package com.zhuhodor.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.Entity.Project;
import com.zhuhodor.myblog.Entity.User;
import com.zhuhodor.myblog.vo.RequestVo;
import com.zhuhodor.myblog.vo.TableVo;
import org.apache.ibatis.annotations.*;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
@Mapper
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

    /**
     * 提交申请
     * @param userId
     * @param projectId
     */
    @Insert("INSERT INTO project_contributor(project_id, contributor_id, confirm) values (#{projectId}, #{userId}, 0)")
    void request(String userId, String projectId);

    /**
     * 是否已经申请过
     * @param userId
     * @param projectId
     * @return
     */
    @Select("SELECT project_id from project_contributor WHERE project_id = #{projectId} AND contributor_id = #{userId}")
    String isRequested(String userId, String projectId);

    /**
     * 获取加入项目申请列表
     * @param userId
     * @return
     */
    @Select("SELECT pc.project_id, pc.contributor_id, p.project_name, u.username FROM project_contributor pc, project p, user u WHERE " +
            "p.id = pc.project_id and p.start_user = u.id and p.start_user = #{userId} and pc.confirm = 0")
    List<RequestVo> findRequestsByUserId(String userId);


    /**
     * 同意申请
     * @param projectId
     * @param contributorId
     * @param now
     * @return
     */
    @Update("UPDATE project_contributor SET confirm = 1, date = #{now} WHERE project_id = #{projectId} AND contributor_id = #{contributorId}")
    boolean confirm(String projectId, String contributorId, Date now);

    /**
     * 拒绝申请
     * @param projectId
     * @param contributorId
     * @return
     */
    @Delete("DELETE FROM project_contributor WHERE project_id = #{projectId} AND contributor_id = #{contributorId} AND confirm = 0")
    boolean reject(String projectId, String contributorId);

    /**
     * 能否编辑
     * @param projectId
     * @param userId
     * @return
     */
    @Select("SELECT confirm from project_contributor WHERE project_id = #{projectId} and contributor_id = #{userId}")
    Integer editable(String projectId, String userId);

    /**
     * 贡献成员信息表
     * @param projectId
     * @return
     */
    @Select("SELECT u.id as user_id , u.username, pc.date  FROM `project_contributor` pc, user u WHERE u.id = pc.contributor_id AND pc.project_id = #{projectId} AND pc.confirm = 1")
    List<TableVo> contributorTable(String projectId);

    /**
     * 查询次数
     * @param projectId
     * @param userId
     * @return
     */
    @Select("SELECT count(*) FROM project_blog pb, blog b WHERE pb.project_id = #{projectId} AND pb.blog_id = b.id AND b.userId = #{userId}")
    Integer countNumber(String projectId, String userId);

}
