package com.zhuhodor.myblog.controller;

import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.common.Result;
import com.zhuhodor.myblog.vo.searchRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController extends BaseController {

    @PostMapping("/query")
    public Result search(@RequestBody searchRequest query){
        log.info("搜索条件为===={}====", query.getQuerystr());
        List<Blog> esBlogs = esBlogService.searchBlog(query);
        return Result.success(esBlogs);
    }
}
