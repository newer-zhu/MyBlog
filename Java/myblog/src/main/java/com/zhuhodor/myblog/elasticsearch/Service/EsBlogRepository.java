package com.zhuhodor.myblog.elasticsearch.Service;

import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.elasticsearch.Entity.EsBlog;
import com.zhuhodor.myblog.vo.searchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;


@Service
public interface EsBlogRepository extends ElasticsearchRepository<EsBlog, String> {

}
