package com.zhuhodor.myblog.controller;

import com.zhuhodor.myblog.AI.TextAnalysis;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/AI")
@Slf4j
public class AIController extends BaseController{

    @PostMapping("/summary")
    public Result getSummary(@RequestBody Blog blog){
        return Result.success(TextAnalysis.autoSummary(blog.getContent()));
    }

    @GetMapping("/similar/{text}")
    public Result similarWords(@PathVariable String text){
        return Result.success(TextAnalysis.similarWords(text));
    }
}
