package com.zhuhodor.myblog.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.zhuhodor.myblog.common.Result;
import org.springframework.web.bind.annotation.*;

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
}
