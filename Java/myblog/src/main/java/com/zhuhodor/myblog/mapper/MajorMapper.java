package com.zhuhodor.myblog.mapper;

import com.zhuhodor.myblog.vo.MajorOpVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
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
}
