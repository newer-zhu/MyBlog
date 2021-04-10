package com.zhuhodor.myblog.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.zhuhodor.myblog.common.Result;
import org.springframework.web.bind.annotation.*;

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
}
