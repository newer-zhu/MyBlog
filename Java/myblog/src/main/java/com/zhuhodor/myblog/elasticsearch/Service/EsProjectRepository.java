package com.zhuhodor.myblog.elasticsearch.Service;

import com.zhuhodor.myblog.elasticsearch.Entity.EsProject;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

@Service
public interface EsProjectRepository extends ElasticsearchRepository<EsProject, Integer> {
}
