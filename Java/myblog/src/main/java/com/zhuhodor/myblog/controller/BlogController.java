package com.zhuhodor.myblog.controller;

import com.zhuhodor.myblog.Entity.Blog;
import com.zhuhodor.myblog.Entity.Blog_Column;
import com.zhuhodor.myblog.common.Result;
import com.zhuhodor.myblog.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;


@RestController
@RequestMapping("blog")
@Slf4j
public class BlogController {
    @Autowired
    BlogService blogService;

    @GetMapping("getpages")
    public int getPages(String userId){
        return blogService.getPagesByUserId(userId);
    }

    @GetMapping("/getbyuserid")
    public Result listBlogsByUserId(@RequestParam("userId") String userId){
//        PageHelper.startPage(1,2);
        log.info("查询用户博客");
        List<Blog> blogs = blogService.findBlogsByUserId(userId);
        return Result.success(blogs);
    }

    /**
     * 储存博客
     * @param blog
     * @return
     */
    @PostMapping("/saveblog")
    public Result saveBlog(@RequestBody @Validated Blog blog){
        try {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            blog.setCreatedAt(now);
            blogService.saveBlog(blog);
            log.info("储存id为 {}的博客", blog.id);
            Blog mBlog = blogService.findBlogById(blog.id);
            return Result.success(mBlog);
        }catch (Exception e){
            log.error("save blog error");
        }
        return Result.fail(500,"error",null);
    }

    @PostMapping("/modifyblog")
    public Result modifyBlog(@RequestBody @Validated Blog blog){
        try {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            blog.setCreatedAt(now);
            blogService.editBlog(blog);
            log.info("修改id为 {}的博客", blog.id);
            Blog mBlog = blogService.findBlogById(blog.id);
            return Result.success(mBlog);
        }catch (Exception e){
            log.error("save blog error");
        }
        return Result.fail(500,"error",null);
    }

    @GetMapping("/getbyid/{id}")
    public Result getBlogById(@PathVariable("id") String id){
        Blog blog = blogService.findBlogById(id);
        if (blog != null){
            return Result.success(blog);
        }else {
            log.info("id为 {}的博客为空", id);
            return Result.success(null);
        }
    }

    @GetMapping("/getColumnByUserId/{userId}")
    public Result getColumnByUserId(@PathVariable("userId") String userId){
        log.info("查询userId={}的博客分栏", userId);
        List<Blog_Column> columnsName = blogService.getColumnsByUserId(userId);
        if (columnsName != null){
            return Result.success(columnsName);
        }else {
            return Result.fail("还未创建分栏");
        }
    }

    @RequestMapping("blogToColumn")
    public Result BlogToColumn(@RequestParam("blogId") String blogId, @RequestParam("cid") String cid){
        log.info("为bid={},cid={}博客添加分类",blogId,cid);
        try {
            blogService.blogToColumn(blogId, cid);
            return Result.success("ok");
        }catch (Exception e){
            log.error("分类错误");
            return Result.fail("error");
        }
    }
}