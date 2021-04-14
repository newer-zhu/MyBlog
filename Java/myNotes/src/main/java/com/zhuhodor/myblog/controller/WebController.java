package com.zhuhodor.myblog.controller;

import com.zhuhodor.myblog.common.Result;
import com.zhuhodor.myblog.service.WebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

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


}
