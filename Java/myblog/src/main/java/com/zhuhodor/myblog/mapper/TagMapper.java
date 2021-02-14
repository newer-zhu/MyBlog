package com.zhuhodor.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuhodor.myblog.Entity.Tag;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("tagMapper")
public interface TagMapper extends BaseMapper<Tag> {

    @Select("SELECT t.* FROM `tag` t, blog_tag bt WHERE t.id = bt.tag_id AND bt.blog_id = #{blogId}")
    List<Tag> getTagsbyBlogId(String blogId);

    @Delete("DELETE FROM blog_tag WHERE blog_id = #{blogId}")
    boolean removeBlogsInTag(String blogId);

    @Insert("INSERT INTO blog_tag VALUES(#{blogId}, #{tagId})")
    boolean blogIntoTags(String blogId, String tagId);
}
