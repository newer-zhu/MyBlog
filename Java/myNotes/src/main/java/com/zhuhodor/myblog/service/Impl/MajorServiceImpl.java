package com.zhuhodor.myblog.service.Impl;

import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.mapper.MajorMapper;
import com.zhuhodor.myblog.service.MajorService;
import com.zhuhodor.myblog.util.RedisUtils;
import com.zhuhodor.myblog.vo.MajorOpVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MajorServiceImpl implements MajorService {
    @Autowired
    MajorMapper majorMapper;

    @Autowired
    RedisUtils redisUtils;

    public List<MajorOpVo> findAll() {
        List<MajorOpVo> parent = majorMapper.findAllParent();
        parent.forEach(m -> {
            m.setChildren(majorMapper.findMajorByPid(m.getId()));
        });
        return parent;
    }

    @Override
    public boolean insert(String blogId, String majorId) {
        if (!redisUtils.hexists("major", majorId)){
            redisUtils.hset("major", majorId, String.valueOf(1));
        }else {
            redisUtils.hincrby("major", majorId, 1L);
        }
        majorMapper.releaseMajor(blogId);
        return majorMapper.insert(blogId, majorId);
    }

    @Override
    public List<MajorOpVo> topMajors() {
        return majorMapper.topMajors();
    }

    @Override
    public List<Integer> findAllId() {
        return majorMapper.findAllId();
    }

    @Override
    public void updatePopular(String id, String popular) {
        majorMapper.updatePopular(id, popular);
    }

    @Override
    public List<Blog> majorBlog(String name) {
        return majorMapper.majorBlog(name);
    }
}
