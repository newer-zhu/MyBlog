package com.zhuhodor.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.Entity.ProjectModule.Project;
import com.zhuhodor.myblog.vo.RequestVo;
import com.zhuhodor.myblog.vo.TableVo;

import java.util.Date;
import java.util.List;

public interface ProjectService extends IService<Project> {

    List<Date> getBlogTime(String projectId);

    List<Blog> findBlogsByProjectId(String projectId);

    boolean favorite(String userId, String projectId);

    boolean cancel(String userId, String projectId);

    List<Project> getFavoriteByUserId(String userId);

    int isFavorite(String userId, String projectId);

    Integer favoriteCount(String projectId);

    Project getProjectByBlogId(String blogId);

    boolean request(String userId, String projectId);

    List<RequestVo> findRequestsByUserId(String userId);

    void dealRequest(String projectId, String userId, String res);

    List<TableVo> contributorTable(String projectId);

    boolean editable(String projectId, String userId);
}
