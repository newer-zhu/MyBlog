package com.zhuhodor.myblog.elasticsearch.Service;

import com.zhuhodor.myblog.elasticsearch.Entity.EsBlog;
import org.springframework.stereotype.Service;

@Service
public interface EsBlogService {
    void insertBlog(EsBlog blog);

    void delBlog(String id);

    void updateBlog(EsBlog blog);
}
