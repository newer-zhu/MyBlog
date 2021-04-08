package com.zhuhodor.myblog.service;


import com.zhuhodor.myblog.vo.MajorOpVo;

import java.util.List;

public interface MajorService {
    List<MajorOpVo> findAll();

    boolean insert(String blogId, String majorId);
}
