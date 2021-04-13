package com.zhuhodor.myblog.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.vo.MajorOpVo;

import java.util.List;

public interface MajorService {
    List<MajorOpVo> findAll();

    boolean insert(String blogId, String majorId);

    List<MajorOpVo> topMajors();

    List<Integer> findAllId();

    void updatePopular(String id, String popular);

    List<Blog> majorBlog(String name);
}
