package com.zhuhodor.myblog.test;

import com.zhuhodor.myblog.AI.TextAnalysis;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AItest {
    @Autowired
    TextAnalysis textAnalysis;

    @Test
    public void test(){
        textAnalysis.wordRelation("软件工程");
    }
}
