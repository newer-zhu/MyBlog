package com.zhuhodor.myblog.controller;

import com.zhuhodor.myblog.AI.TextAnalysis;
import com.zhuhodor.myblog.elasticsearch.Service.EsBlogRepository;
import com.zhuhodor.myblog.elasticsearch.Service.EsProjectRepository;
import com.zhuhodor.myblog.elasticsearch.Service.EsService;
import com.zhuhodor.myblog.service.*;
import com.zhuhodor.myblog.util.JwtUtil;
import com.zhuhodor.myblog.util.RedisUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BaseController {
    @Autowired
    UserService userService;

    @Autowired
    BlogService blogService;

    @Autowired
    CommentService commentService;

    @Autowired
    ProjectService projectService;

    @Autowired
    TagService tagService;

    @Autowired
    ColumnService columnService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    EsBlogRepository esBlogRepository;

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    EsService esBlogService;

    @Autowired
    EsProjectRepository esProjectRepository;

    @Autowired
    TextAnalysis keywordsExtraction;

    @Autowired
    RabbitTemplate rabbitTemplate;

}
