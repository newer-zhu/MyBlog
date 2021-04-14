package com.zhuhodor.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuhodor.myblog.Entity.BlogModule.BlogColumn;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ColumnMapper extends BaseMapper<BlogColumn>{
    @Insert("INSERT INTO blog_column(c_name, user_id) VALUES (#{c_name}, #{user_id})")
    boolean createBlogColumn(BlogColumn blog_column);

    @Insert("INSERT INTO blog_with_column(blog_id, column_id) VALUES(#{blog_id}, #{column_id})")
    boolean blogToColumn(String blog_id, String column_id);

    @Select("SELECT * from blog_column where user_id = #{userId}")
    List<BlogColumn> getColumnsByUserId(String userId);

    @Select("SELECT bc.* FROM blog_column bc, blog_with_column bwc WHERE " +
            "bwc.blog_id = #{blogId} AND bwc.column_id = bc.id AND bc.user_id = #{userId}")
    BlogColumn getColumnByBlogId(String blogId, String userId);

    @Delete("DELETE FROM blog_with_column WHERE blog_id = #{blogId} AND column_id = #{columnId}")
    void delBlogInColumn(String blogId, String columnId);

    @Delete("DELETE FROM blog_column WHERE id = #{column_id}")
    boolean delColumnById(String column_id);

    @Select("SELECT count(*) FROM blog_with_column WHERE column_id = #{column_id}")
    Integer count(int column_id);
}
