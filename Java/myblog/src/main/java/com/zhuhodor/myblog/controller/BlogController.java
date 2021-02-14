package com.zhuhodor.myblog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.common.Result;
import com.zhuhodor.myblog.elasticsearch.Entity.EsBlog;
import com.zhuhodor.myblog.elasticsearch.Service.EsBlogService;
import com.zhuhodor.myblog.service.BlogService;
import com.zhuhodor.myblog.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/blog")
@Slf4j
public class BlogController {

    @Autowired
    EsBlogService esBlogService;
    @Autowired
    BlogService blogService;
    @Autowired
    RedisUtils redisUtils;

    /**
     * 根据用户ID分页获取博客结果
     * @param page
     * @param limit
     * @param userId
     * @return
     */
    @GetMapping("/getpagesbyuserid/{userId}/")
    public Result getPages(@RequestParam(value="page",defaultValue="1")Integer page,
                        @RequestParam(value = "limit", defaultValue = "5") Integer limit,@PathVariable("userId") String userId){
        PageHelper.startPage(page, limit);
        List<Blog> blogList = blogService.findBlogsByUserId(userId);
        PageInfo pageInfo = new PageInfo(blogList);
        return Result.success(blogList);
    }

    /**
     * 根据博客ID删除博客
     * @param blogId
     * @return
     */
    @GetMapping("/delblogbyid")
    public Result delBlogById(@RequestParam("blogId") String blogId){
        log.info("用户删除了id为{}的博客",blogId);
        blogService.delBlog(blogId);
        esBlogService.delBlog(blogId);
        redisUtils.del("blogVisitors:"+blogId);
        return Result.success("删除成功");
    }

    /**
     * 根据用户ID获取博客
     * @param userId
     * @return
     */
    @GetMapping("/getbyuserid/{userId}")
    public Result listBlogsByUserId(@PathVariable("userId") String userId){
//        PageHelper.startPage(1,2);
        log.info("查询用户博客");
        List<Blog> blogs = blogService.findBlogsByUserId(userId);
        return Result.success(blogs);
    }

    /**
     * 新建博客
     * @param blog
     * @return
     */
    @PostMapping("/saveblog")
    public Result saveBlog(@RequestBody @Validated Blog blog){
        log.info("储存id为 {}的博客", blog.id);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        blog.setCreatedAt(now);
        blogService.saveBlog(blog);
        Blog mBlog = blogService.findBlogById(blog.id);
        EsBlog esBlog = new EsBlog();
        BeanUtils.copyProperties(mBlog, esBlog);
        esBlogService.insertBlog(esBlog);
        return Result.success(mBlog);
    }

    /**
     * 修改博客
     * @param blog
     * @return
     */
    @PostMapping("/modifyblog")
    public Result modifyBlog(@RequestBody @Validated Blog blog){
        Timestamp now = new Timestamp(System.currentTimeMillis());
        blog.setCreatedAt(now);
        blogService.editBlog(blog);
        log.info("修改id为 {}的博客", blog.id);
        Blog mBlog = blogService.findBlogById(blog.id);
        return Result.success(mBlog);
    }

    /**
     * 根据博客ID获取博客
     * @param id
     * @return
     */
    @GetMapping("/getbyid/{id}")
    public Result getBlogById(@PathVariable("id") String id){
        Blog blog = blogService.findBlogById(id);
        if (!redisUtils.hasKey("blogVisitors:"+id)){
            redisUtils.set("blogVisitors:"+id, String.valueOf(1));
        }else {
            redisUtils.incr("blogVisitors:"+id, 1L);
        }
        if (blog != null){
            blog.setVisitors(Integer.valueOf(redisUtils.get("blogVisitors:"+id)));
            return Result.success(blog);
        }else {
            return Result.success(null);
        }
    }

    /**
     * 根据分栏ID获取博客分页
     */
    @GetMapping("/getpagesbycolumnid/{columnId}")
    public Result getPagesByColumnId(@PathVariable("columnId") String columnId,
                                     @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit,
                                     @RequestParam(value = "page", required = false, defaultValue = "1") Integer page){
//        IPage<Blog> page = blogService.getPagesByColumnId(new Page<Blog>(1, 2), columnId);
        PageHelper.startPage(page, limit);
        List<Blog> pageRecord = blogService.getPagesByColumnId(columnId);
        return Result.success(pageRecord);
    }


}
