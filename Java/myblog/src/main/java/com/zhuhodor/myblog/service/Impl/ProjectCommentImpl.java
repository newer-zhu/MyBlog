package com.zhuhodor.myblog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuhodor.myblog.Entity.ProjectComment;
import com.zhuhodor.myblog.mapper.ProjectCommentMapper;
import com.zhuhodor.myblog.service.ProjectCommentService;
import org.springframework.stereotype.Service;

@Service
public class ProjectCommentImpl extends ServiceImpl<ProjectCommentMapper, ProjectComment> implements ProjectCommentService {

}
