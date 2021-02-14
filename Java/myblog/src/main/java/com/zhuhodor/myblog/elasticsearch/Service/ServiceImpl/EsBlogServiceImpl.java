package com.zhuhodor.myblog.elasticsearch.Service.ServiceImpl;

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhuhodor.myblog.elasticsearch.Entity.EsBlog;
import com.zhuhodor.myblog.elasticsearch.Service.EsBlogService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;

@Component
@Slf4j
public class EsBlogServiceImpl implements EsBlogService {
    @Autowired
    RestHighLevelClient restHighLevelClient;

    /**
     * 往Es中插入博客
     * @param blog
     */
    @Override
    public void insertBlog(EsBlog blog) {
        log.info("插入博客 {} 到ES",blog.getId());
        IndexRequest indexRequest = new IndexRequest("blog", "_doc", blog.getId())
                .source("id", blog.getId(),
                        "content", blog.getContent(),
                        "createdAt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(blog.getCreatedAt()).toString(),
                        "summary", blog.getSummary(),
                        "title", blog.getTitle(),
                        "userId", blog.getUserId(),
                        "isFile", blog.getIsFile(),
                        "tags", blog.getTags());
        try {
            IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除Es中的博客
     * @param id
     */
    @Override
    public void delBlog(String id){
        DeleteRequest deleteRequest = new DeleteRequest("blog", "_doc", id);
        try {
            restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新Es中的博客
     */
    @Override
    public void updateBlog(EsBlog blog){
        UpdateRequest updateRequest = new UpdateRequest("blog", "_doc", blog.getId()).doc(
                "content", blog.getContent(),
                "createdAt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(blog.getCreatedAt()).toString(),
                "summary", blog.getSummary(),
                "title", blog.getTitle(),
                "userId", blog.getUserId(),
                "isFile", blog.getIsFile(),
                "tags", blog.getTags());
        try {
            restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
