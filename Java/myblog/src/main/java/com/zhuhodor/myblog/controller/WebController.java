package com.zhuhodor.myblog.controller;

import com.zhuhodor.myblog.Entity.Event;
import com.zhuhodor.myblog.Entity.User;
import com.zhuhodor.myblog.common.Result;
import com.zhuhodor.myblog.service.UserService;
import com.zhuhodor.myblog.service.WebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/web")
public class WebController extends BaseController{

    @Autowired
    private WebService webService;

    @PostMapping("/avatarUpload")
    public Result avatarUpload(@RequestParam("avatarPic") MultipartFile avatar, HttpServletRequest req){
        log.info("upload  start ----> {}",avatar);
        Result result = webService.upload(avatar, req);
        return result;
    }

    @PostMapping("/timeline")
    public Result timeline(@RequestBody  Event event){
        event.setTime(LocalDateTime.now());
        event.setLevel(0);
        log.info("收到了事件-->{}",event.getContent());
        return Result.success(event);
    }

}
