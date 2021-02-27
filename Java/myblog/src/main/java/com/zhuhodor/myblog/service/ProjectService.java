package com.zhuhodor.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.Entity.Project;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService extends IService<Project> {


    List<Blog> findBlogsByProjectId(String projectId);

    boolean favorite(String userId, String projectId);

    boolean cancel(String userId, String projectId);

    List<Project> getFavoriteByUserId(String userId);

    int isFavorite(String userId, String projectId);

    Integer favoriteCount(String projectId);

    Project getProjectByBlogId(String blogId);
}
