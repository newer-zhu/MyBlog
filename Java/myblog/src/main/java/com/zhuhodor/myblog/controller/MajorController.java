package com.zhuhodor.myblog.controller;

import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学科相关控制器
 */
@RestController
@RequestMapping("/major")
public class MajorController extends BaseController {
    @GetMapping()
    public Result getAll(){
        return Result.success(majorService.findAll());
    }

    /**
     * 关联学科
     * @param blogId
     * @param majorId
     * @return
     */
    @GetMapping("/insert/{blogId}/{majorId}")
    public Result insert(@PathVariable("blogId") String blogId, @PathVariable("majorId") String majorId){
        if (majorService.insert(blogId, majorId)){
            return Result.success(null);
        }
        return null;
    }

    /**
     * 热门学科
     */
    @GetMapping("/top")
    public Result top(){
        return Result.success(majorService.topMajors());
    }

    /**
     * 专业相关文章
     */
    @GetMapping("/name/{name}")
    public Result majorBlog(@PathVariable String name, @RequestParam("page") Integer page){
        PageHelper.startPage(page, 5);
        List<Blog> blogs = majorService.majorBlog(name);
        PageInfo pageInfo = new PageInfo(blogs);
        return Result.success(MapUtil.builder()
                .put("blogList", blogs)
                .put("total", pageInfo.getPages()).map());
    }
}
