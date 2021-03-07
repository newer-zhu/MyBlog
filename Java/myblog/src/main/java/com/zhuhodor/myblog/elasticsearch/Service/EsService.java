package com.zhuhodor.myblog.elasticsearch.Service;

import com.zhuhodor.myblog.Entity.Project;
import com.zhuhodor.myblog.elasticsearch.Entity.EsProject;
import com.zhuhodor.myblog.vo.searchRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("esBlogService")
public interface EsService {
    Map<String, Object> searchBlog(searchRequest query);

    List<Project> searchProject(searchRequest query);
}
