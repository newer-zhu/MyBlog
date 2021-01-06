package com.zhuhodor.myblog.controller;

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
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/web")
public class WebController {
    @Autowired
    private UserService userService;

    @Autowired
    private WebService webService;

    @PostMapping("/avatarUpload")
    public Result avatarUpload(@RequestParam("avatarPic") MultipartFile avatar, HttpServletRequest req){
        log.info("upload  start ----> {}",avatar);
        Result result = webService.upload(avatar, req);
        return result;
    }

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String rigister(){
        return "register";
    }
}
