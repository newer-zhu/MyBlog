package com.zhuhodor.myblog.controller;

import com.zhuhodor.myblog.AI.KeywordsExtraction;
import com.zhuhodor.myblog.elasticsearch.Service.EsBlogRepository;
import com.zhuhodor.myblog.elasticsearch.Service.EsBlogService;
import com.zhuhodor.myblog.service.*;
import com.zhuhodor.myblog.util.JwtUtil;
import com.zhuhodor.myblog.util.RedisUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

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
    EsBlogService esBlogService;

    @Autowired
    KeywordsExtraction keywordsExtraction;

}
