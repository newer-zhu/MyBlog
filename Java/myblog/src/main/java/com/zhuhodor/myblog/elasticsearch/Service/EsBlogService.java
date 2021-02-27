package com.zhuhodor.myblog.elasticsearch.Service;

import com.zhuhodor.myblog.elasticsearch.Entity.EsBlog;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EsBlogService {
    List<EsBlog> searchBlog(String query);


    String findBlogByString(String query);
}
