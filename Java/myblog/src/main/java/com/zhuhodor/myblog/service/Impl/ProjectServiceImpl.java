package com.zhuhodor.myblog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.Entity.ProjectModule.Project;
import com.zhuhodor.myblog.mapper.ProjectMapper;
import com.zhuhodor.myblog.service.ProjectService;
import com.zhuhodor.myblog.vo.RequestVo;
import com.zhuhodor.myblog.vo.TableVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService{
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    ProjectMapper projectMapper;

    @Override
    public List<Date> getBlogTime(String projectId) {
        return projectMapper.getBlogTime(projectId);
    }

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

    @Override
    public boolean request(String userId, String projectId) {
        if(StringUtils.isEmpty(projectMapper.isRequested(userId, projectId))){
            projectMapper.request(userId, projectId);
            rabbitTemplate.convertAndSend("project","project.sendmail", projectId+":"+userId);
            return false;
        }else {
            return true;
        }
    }

    @Override
    public List<RequestVo> findRequestsByUserId(String userId) {
        return projectMapper.findRequestsByUserId(userId);
    }

    @Override
    public void dealRequest(String projectId, String contributorId, String res) {
        if (res.equals("0")){
            projectMapper.reject(projectId, contributorId);
        }else {
            Date now = Calendar.getInstance().getTime();
            projectMapper.confirm(projectId, contributorId, now);
        }
    }

//    @Override
//    public Integer isConfirm(String projectId, String userId) {
//        return projectMapper.isConfirm(projectId, userId);
//    }

    @Override
    public List<TableVo> contributorTable(String projectId) {
        List<TableVo> table = projectMapper.contributorTable(projectId);
        for (TableVo t : table)
            t.setNumber(projectMapper.countNumber(projectId, String.valueOf(t.getUserId())));
        return table;
    }

    @Override
    public boolean editable(String projectId, String userId) {
        Integer i = projectMapper.editable(projectId, userId);
        if (null == i|| i == 0)
            return false;
        else
            return true;
    }
}
