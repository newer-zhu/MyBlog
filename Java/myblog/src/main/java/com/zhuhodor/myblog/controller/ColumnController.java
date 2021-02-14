package com.zhuhodor.myblog.controller;

import com.zhuhodor.myblog.Entity.BlogModule.BlogColumn;
import com.zhuhodor.myblog.common.Result;
import com.zhuhodor.myblog.service.ColumnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/column")
@Slf4j
public class ColumnController {
    @Autowired
    ColumnService columnService;

//    //联系博客到分栏
//    @PostMapping("/blogtocolumn")
//    public Result BlogToColumn(@RequestBody ColumnList columnList){
//        String blogId = columnList.getBlogId();
//        log.info("为bid={} 博客修改分栏",blogId);
//        List<String> after = columnList.getAfter();
//        List<String> before = columnList.getBefore();
//        System.out.println(after);
//        System.out.println(before);
//        try {
//            for (String a : after){
//                if (!before.contains(a))
//                    columnService.blogToColumn(blogId, a);
//            }
//            for (String b : before){
//                if (!after.contains(b))
//                    columnService.delBlogInColumn(blogId,b);
//            }
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            return Result.fail("修改时发送错误");
//        }
//        return Result.success(null);
//    }
    //联系博客到分栏
    @GetMapping("/blogtocolumn/{blogId}/{columnId}")
    public Result BlogToColumn(@PathVariable("blogId") String blogId,
                               @PathVariable("columnId") String columnId){
        columnService.delBlogInColumn(blogId);
        columnService.blogToColumn(blogId,columnId);
        return Result.success("操作成功");
    }

    @GetMapping("/getcolumnbyblogid/{blogId}")
    public Result getColumnByBlogId(@PathVariable("blogId") String blogId){
        log.info("查询blogId---{}的专栏");
        return Result.success(columnService.getColumnByBlogId(blogId));
    }


    @GetMapping("/getcolumnbyuserid/{userId}")
    public Result getColumnByUserId(@PathVariable("userId") String userId){
        log.info("查询userId={}的博客分栏", userId);
        List<BlogColumn> columns = columnService.getColumnsByUserId(userId);
        for (BlogColumn b : columns){
            b.setCount(columnService.count(b.getId()));
        }
        if (columns != null){
            return Result.success(columns);
        }else {
            return Result.fail("还未创建分栏");
        }
    }

//    @PostMapping("/delcolumnbyid")
//    public Result delColumnById(@RequestBody String[] column_id) {
//        log.info("删除第{}号专栏", column_id);
//        for (String s : column_id) {
//            columnService.delColumnById(s);
//        }
//        return Result.success("删除成功");
//    }
}
