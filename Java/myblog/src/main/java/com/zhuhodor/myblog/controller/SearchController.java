package com.zhuhodor.myblog.controller;

import cn.hutool.core.map.MapUtil;
import com.github.houbb.segment.support.segment.result.impl.SegmentResultHandler;
import com.github.houbb.segment.support.segment.result.impl.SegmentResultHandlers;
import com.github.houbb.segment.util.SegmentHelper;
import com.zhuhodor.myblog.Entity.Project;
import com.zhuhodor.myblog.common.Result;
import com.zhuhodor.myblog.vo.searchRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController extends BaseController {

    @PostMapping("/query")
    public Result search(@RequestBody searchRequest query){
        log.info("搜索条件为===={}====", query.getQuerystr());
        List<String> segment = SegmentHelper.segment(query.getQuerystr(), SegmentResultHandlers.word());
        segment.forEach(word -> {
            if (word.length() > 1){
                redisUtils.zincr("keywords", word, 0.01);
            }
        });
        Map<String, Object> map = esBlogService.searchBlog(query);
        List<Project> projects = esBlogService.searchProject(query);
        return Result.success(MapUtil.builder().put("blogList", map.get("blogList"))
        .put("total", map.get("total"))
        .put("projects", projects).map());
    }

    @GetMapping("/words")
    public Result getWords(){
        return Result.success(redisUtils.revRangeWithScore("keywords", 0L, 10L));
    }
}
