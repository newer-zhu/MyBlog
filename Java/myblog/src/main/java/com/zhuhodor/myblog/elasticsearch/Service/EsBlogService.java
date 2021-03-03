package com.zhuhodor.myblog.elasticsearch.Service;

import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.vo.searchRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("esBlogService")
public interface EsBlogService {
    List<Blog> searchBlog(searchRequest query);

}
