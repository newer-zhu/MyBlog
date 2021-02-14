package com.zhuhodor.myblog.test;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class EsTest {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    void testEs() throws IOException {
        CreateIndexRequest blog = new CreateIndexRequest("blog");
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(blog, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse.index());
    }

    @Test
    void delTest() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("blog");
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }
}
