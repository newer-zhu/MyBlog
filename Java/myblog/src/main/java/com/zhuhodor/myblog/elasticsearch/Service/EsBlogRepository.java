package com.zhuhodor.myblog.elasticsearch.Service;

import com.zhuhodor.myblog.elasticsearch.Entity.EsBlog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

@Service
public interface EsBlogRepository extends ElasticsearchRepository<EsBlog, String> {
}
