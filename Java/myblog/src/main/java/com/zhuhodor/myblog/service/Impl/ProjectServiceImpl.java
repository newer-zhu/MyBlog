package com.zhuhodor.myblog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuhodor.myblog.Entity.Project;
import com.zhuhodor.myblog.mapper.ProjectMapper;
import com.zhuhodor.myblog.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;

public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService{

}
