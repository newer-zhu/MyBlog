package com.zhuhodor.myblog.service.Impl;

import com.zhuhodor.myblog.mapper.MajorMapper;
import com.zhuhodor.myblog.service.MajorService;
import com.zhuhodor.myblog.vo.MajorOpVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MajorServiceImpl implements MajorService {
    @Autowired
    MajorMapper majorMapper;

    public List<MajorOpVo> findAll() {
        List<MajorOpVo> parent = majorMapper.findAllParent();
        parent.forEach(m -> {
            m.setChildren(majorMapper.findMajorByPid(m.getId()));
        });
        return parent;
    }

    @Override
    public boolean insert(String blogId, String majorId) {
        return majorMapper.insert(blogId, majorId);
    }
}
