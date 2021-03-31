package com.zhuhodor.myblog.controller;

import com.zhuhodor.myblog.common.Result;
import com.zhuhodor.myblog.vo.RequestVo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController extends BaseController{
    @GetMapping("/{userId}")
    public Result sendMessage(@PathVariable("userId") String userId){
        List<RequestVo> requests = projectService.findRequestsByUserId(userId);
        return Result.success(requests);
    }
}
