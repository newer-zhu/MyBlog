package com.zhuhodor.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuhodor.myblog.Entity.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("SELECT c.* FROM `comment` c, `rel_comment` rc WHERE  c.id = " +
            "rc.comment_id AND ISNULL(rc.target_comment) AND rc.target_blog = #{blogId} ORDER BY create_date DESC")
    List<Comment> selectCommentByBlogId(int blogId);

    @Select("SELECT c.* FROM `comment` c, `rel_comment` rc WHERE  c.id = rc.comment_id " +
            "AND rc.target_comment = #{comment_id} ORDER BY create_date DESC")
    List<Comment> selectChildrenByCommentId(int comment_id);

    @Insert("INSERT INTO `rel_comment`(comment_id, target_blog) Values(#{commentId}, #{blogId})")
    boolean commentToBlog(int commentId, int blogId);

    @Insert("INSERT INTO `rel_comment`(comment_id, target_comment) Values(#{commentId}, #{targetComment})")
    boolean childCommentToParent(int commentId, int targetComment);
}
