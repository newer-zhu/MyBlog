package com.zhuhodor.myblog.elasticsearch.Service.ServiceImpl;

import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.elasticsearch.Service.EsBlogRepository;
import com.zhuhodor.myblog.elasticsearch.Service.EsBlogService;
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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class EsBlogServiceImpl implements EsBlogService {
    @Autowired
    RestHighLevelClient restHighLevelClient;
    @Autowired
    UserService userService;

    private static final Integer LIMIT = 1;
    @Override
    public List<Blog> searchBlog(searchRequest query) {
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
        if (!StringUtils.isEmpty(query.getUserId()) && query.getUserId() != "")
            boolQueryBuilder.must(QueryBuilders.termQuery("userId", 1));
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("createdAt");
        if (!StringUtils.isEmpty(query.getStart()) && query.getStart() != ""){
            rangeQueryBuilder.from(query.getStart());
        }
        if (!StringUtils.isEmpty(query.getEnd()) && query.getEnd() != ""){
            rangeQueryBuilder.to(query.getEnd());
        }
        boolQueryBuilder.must(rangeQueryBuilder);
        searchSourceBuilder.postFilter(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        ArrayList<Blog> resBlogs = new ArrayList<>();
        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
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
        return resBlogs;
    }
}
