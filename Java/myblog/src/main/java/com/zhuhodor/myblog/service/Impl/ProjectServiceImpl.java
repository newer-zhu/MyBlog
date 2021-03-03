package com.zhuhodor.myblog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.Entity.Project;
import com.zhuhodor.myblog.mapper.ProjectMapper;
import com.zhuhodor.myblog.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService{

    @Autowired
    ProjectMapper projectMapper;

    @Override
    public List<Blog> findBlogsByProjectId(String projectId) {
        return projectMapper.findBlogsByProjectId(projectId);
    }

    @Override
    public boolean favorite(String userId, String projectId) {
        return projectMapper.favorite(userId, projectId, LocalDateTime.now().toString());
    }

    @Override
    public boolean cancel(String userId, String projectId) {
        return projectMapper.cancel(userId, projectId);
    }

    @Override
    public List<Project> getFavoriteByUserId(String userId) {
        return projectMapper.getFavoriteByUserId(userId);
    }

    @Override
    public int isFavorite(String userId, String projectId) {
        return projectMapper.isFavorite(userId, projectId);
    }

    @Override
    public Integer favoriteCount(String projectId) {
        return projectMapper.favoriteCount(projectId);
    }

    @Override
    public Project getProjectByBlogId(String blogId) {
        return projectMapper.getProjectByBlogId(blogId);
    }
}
