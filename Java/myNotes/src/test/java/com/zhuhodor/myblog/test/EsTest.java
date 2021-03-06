package com.zhuhodor.myblog.test;

import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.Entity.ProjectModule.Project;
import com.zhuhodor.myblog.elasticsearch.Entity.EsBlog;
import com.zhuhodor.myblog.elasticsearch.Entity.EsProject;
import com.zhuhodor.myblog.elasticsearch.Service.EsBlogRepository;
import com.zhuhodor.myblog.elasticsearch.Service.EsProjectRepository;
import com.zhuhodor.myblog.service.BlogService;
import com.zhuhodor.myblog.service.ProjectService;
import net.bytebuddy.asm.Advice;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EsTest {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    ProjectService projectService;
    @Autowired
    EsProjectRepository esProjectRepository;
    @Autowired
    BlogService blogService;
    @Autowired
    EsBlogRepository esBlogRepository;

    @Test
    public void testBulk(){
        List<Blog> list = blogService.list();
        list.forEach(l -> {
            EsBlog es = new EsBlog();
            BeanUtils.copyProperties(l, es);
            esBlogRepository.save(es);
        });
    }

    @Test
    public void testBulk2(){
        Project p = projectService.getById(11);
        EsProject esProject = new EsProject();
        BeanUtils.copyProperties(p, esProject);
        esProjectRepository.save(esProject);
    }


    @Test
    void testEs() throws IOException {
        SearchRequest searchRequest = new SearchRequest("blog");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("title", "享元"))
        .from(0)
        .size(5)
        .highlighter(new HighlightBuilder().field("*").requireFieldMatch(false).preTags("<span style='color: red'>")
        .postTags("</span>"));
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit h : hits){
            System.out.println(h.getSourceAsMap().get("createdAt"));
            Map<String, HighlightField> highlightFields = h.getHighlightFields();
            if (highlightFields.containsKey("title")){
                System.out.println(highlightFields.get("title").fragments()[0].toString());
            }
        }
    }

    @Test
    void delTest() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("blog");
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }
}
