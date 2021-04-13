package com.zhuhodor.myblog.elasticsearch.Service;

import com.zhuhodor.myblog.Entity.ProjectModule.Project;
import com.zhuhodor.myblog.vo.SearchRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("esBlogService")
public interface EsService {
    Map<String, Object> searchBlog(SearchRequest query);

    List<Project> searchProject(SearchRequest query);
}
