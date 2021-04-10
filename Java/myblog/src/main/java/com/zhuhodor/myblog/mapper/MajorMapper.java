package com.zhuhodor.myblog.mapper;

import com.zhuhodor.myblog.vo.MajorOpVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MajorMapper {
    @Select("SELECT * FROM university_major where parent_id = #{parentId}")
    List<MajorOpVo> findMajorByPid(Integer parentId);

    @Select("SELECT * FROM university_section WHERE id = #{id} ")
    List<MajorOpVo> findParentById(Integer id);

    @Select("SELECT * FROM university_section")
    List<MajorOpVo> findAllParent();

    @Insert("INSERT INTO blog_major(blog_id, major_id) VALUES(#{blogId}, #{majorId})")
    boolean insert(String blogId, String majorId);

    @Select("SELECT * FROM university_major ORDER BY popular DESC LIMIT 0, 10")
    List<MajorOpVo> topMajors();

    @Select("SELECT id FROM university_major")
    List<Integer> findAllId();

    @Update("UPDATE university_major SET popular = #{popular} where id = #{id} ")
    void updatePopular(String id, String popular);

    @Delete("DELETE FROM university_major where blog_id = #{blogId}")
    void releaseMajor(String blogId);
}
