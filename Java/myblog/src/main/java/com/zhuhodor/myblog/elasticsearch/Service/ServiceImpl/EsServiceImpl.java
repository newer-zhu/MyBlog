package com.zhuhodor.myblog.elasticsearch.Service.ServiceImpl;

import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.Entity.Project;
import com.zhuhodor.myblog.elasticsearch.Entity.EsProject;
import com.zhuhodor.myblog.elasticsearch.Service.EsService;
import com.zhuhodor.myblog.service.UserService;
import com.zhuhodor.myblog.vo.searchRequest;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class EsServiceImpl implements EsService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private UserService userService;

    private static final Integer LIMIT = 7;
    @Override
    public Map<String, Object> searchBlog(searchRequest query) {
        SearchRequest searchRequest = new SearchRequest("blog");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(query.getQuerystr(), "title", "content"))
                .sort(new ScoreSortBuilder().order(SortOrder.DESC))
                .from((query.getCurrent()-1)*LIMIT)
                .size(LIMIT)
                .highlighter(new HighlightBuilder().field("*").requireFieldMatch(false).preTags("<span style='color: red'>")
                        .postTags("</span>"));

        //过滤条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(query.getUserId()))
            boolQueryBuilder.must(QueryBuilders.termQuery("userId", query.getUserId()));
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("createdAt");
        if (!StringUtils.isEmpty(query.getStart())){
            rangeQueryBuilder.from(query.getStart());
        }
        if (!StringUtils.isEmpty(query.getEnd())){
            rangeQueryBuilder.to(query.getEnd());
        }
        boolQueryBuilder.must(rangeQueryBuilder);
        searchSourceBuilder.postFilter(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        ArrayList<Blog> resBlogs = new ArrayList<>();
        SearchHits hits = null;
        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            hits = response.getHits();
            for (SearchHit h : hits){
                Blog blog = new Blog();
                Map<String, Object> sourceAsMap = h.getSourceAsMap();
                Map<String, HighlightField> highlightFields = h.getHighlightFields();
                if (highlightFields.containsKey("title")){
                    blog.setTitle(highlightFields.get("title").fragments()[0].toString());
                }else {
                    blog.setTitle((String) sourceAsMap.get("title"));
                }
                if (highlightFields.containsKey("summary")){
                    blog.setSummary(highlightFields.get("summary").fragments()[0].toString());
                }else {
                    blog.setSummary((String) sourceAsMap.get("summary"));
                }
                if (highlightFields.containsKey("content")) {
                    blog.setContent(highlightFields.get("content").fragments()[0].toString());
                }
                blog.setId((String) sourceAsMap.get("id"));
//                User one = userService.getOne(new QueryWrapper<User>().select("id", "username").eq("id", sourceAsMap.get("userId")));
                blog.setUser(userService.findUserById(Integer.parseInt((String) sourceAsMap.get("userId"))));
                blog.setIsFile((Integer) sourceAsMap.get("isFile"));
                blog.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd").parse((String) sourceAsMap.get("createdAt")));
                resBlogs.add(blog);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("blogList", resBlogs);
        if (hits != null)
            map.put("total", hits.getTotalHits().value);
        return map;
    }

    @Override
    public List<Project> searchProject(searchRequest query) {
        SearchRequest searchRequest = new SearchRequest("project");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(query.getQuerystr(), "projectName", "overview"))
                .sort(new ScoreSortBuilder().order(SortOrder.DESC))
                .from((query.getCurrent()-1)*LIMIT)
                .size(LIMIT)
                .highlighter(new HighlightBuilder().field("*").requireFieldMatch(false).preTags("<span style='color: red'>")
                        .postTags("</span>"));

        //过滤条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(query.getUserId()))
            boolQueryBuilder.must(QueryBuilders.termQuery("startUser", query.getUserId()));
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("createAt");
        if (!StringUtils.isEmpty(query.getStart())){
            rangeQueryBuilder.from(query.getStart());
        }
        if (!StringUtils.isEmpty(query.getEnd())){
            rangeQueryBuilder.to(query.getEnd());
        }
        boolQueryBuilder.must(rangeQueryBuilder);
        searchSourceBuilder.postFilter(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        ArrayList<Project> resPros = new ArrayList<>();
        SearchHits hits = null;
        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            hits = response.getHits();
            for (SearchHit h : hits){
                Project pro = new Project();
                Map<String, Object> sourceAsMap = h.getSourceAsMap();
                Map<String, HighlightField> highlightFields = h.getHighlightFields();
                if (highlightFields.containsKey("projectName")){
                    pro.setProjectName(highlightFields.get("projectName").fragments()[0].toString());
                }else {
                    pro.setProjectName((String) sourceAsMap.get("projectName"));
                }
                pro.setId((Integer) sourceAsMap.get("id"));
                pro.setProjectUser(userService.findUserById((Integer) sourceAsMap.get("startUser")));
                pro.setCreateAt(new SimpleDateFormat("yyyy-MM-dd").parse((String) sourceAsMap.get("createAt")));
                pro.setRates((Integer) sourceAsMap.get("rates"));
                resPros.add(pro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resPros;
    }
}
