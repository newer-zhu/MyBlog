package com.zhuhodor.myblog.controller;

import com.zhuhodor.myblog.Entity.Blog_Column;
import com.zhuhodor.myblog.common.Result;
import com.zhuhodor.myblog.service.ColumnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/column")
@Slf4j
public class ColumnController {
    @Autowired
    ColumnService columnService;

    @RequestMapping("/blogToColumn")
    public Result BlogToColumn(@RequestParam("blogId") String blogId, @RequestParam("cid") String cid){
        log.info("为bid={},cid={}博客添加分类",blogId,cid);
        try {
            columnService.blogToColumn(blogId, cid);
            return Result.success("ok");
        }catch (Exception e){
            log.error("分类错误");
            return Result.fail("error");
        }
    }

    @GetMapping("/getcolumnbyuserid/{userId}")
    public Result getColumnByUserId(@PathVariable("userId") String userId){
        log.info("查询userId={}的博客分栏", userId);
        List<Blog_Column> columnsName = columnService.getColumnsByUserId(userId);
        if (columnsName != null){
            return Result.success(columnsName);
        }else {
            return Result.fail("还未创建分栏");
        }
    }
}
