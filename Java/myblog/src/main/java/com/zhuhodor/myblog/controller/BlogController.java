package com.zhuhodor.myblog.controller;

import cn.hutool.core.map.MapUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhuhodor.myblog.AI.KeywordsExtraction;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.common.Result;
import com.zhuhodor.myblog.elasticsearch.Entity.EsBlog;
import com.zhuhodor.myblog.service.BlogService;
import com.zhuhodor.myblog.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/blog")
@Slf4j
public class BlogController extends BaseController{

    /**
     * 根据博客ID删除博客
     * @param blogId
     * @return
     */
    @GetMapping("/delblogbyid")
    public Result delBlogById(@RequestParam("blogId") String blogId){
        log.info("用户删除了id为{}的博客",blogId);
        blogService.delBlog(blogId);
//        esBlogRepository.delete(new EsBlog(blogId));
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
        log.info("查询用户博客");
        List<Blog> blogs = blogService.findBlogsByUserId(userId);
        return Result.success(blogs);
    }

    /**
     * 新建博客
     * @param blog
     * @return
     */
    @PostMapping("/save")
    public Result saveBlog(@RequestBody @Validated Blog blog,
                           @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId){
        log.info("储存id为 {}的博客", blog.id);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        blog.setCreatedAt(now);
        blogService.saveBlog(blog, projectId);
        EsBlog esBlog = new EsBlog();
        BeanUtils.copyProperties(blog, esBlog);
        esBlogRepository.save(esBlog);
        return Result.success(MapUtil.builder().put("blog", blog)
        .put("pushTags", keywordsExtraction.generate(blog.getContent()))
        .put("columns", columnService.getColumnsByUserId(blog.getUserId())).map());
    }

    /**
     * 修改博客
     * @param blog
     * @return
     */
    @PostMapping("/modifyblog")
    public Result modifyBlog(@RequestBody @Validated Blog blog){
        log.info("修改id为 {}的博客", blog.id);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        blog.setCreatedAt(now);
        blogService.editBlog(blog);
        EsBlog esBlog = new EsBlog();
        BeanUtils.copyProperties(blog, esBlog);
        esBlogRepository.save(esBlog);
        return Result.success(MapUtil.builder().put("blog", blog)
                .put("pushTags", keywordsExtraction.generate(blog.getContent()))
                .put("columns", columnService.getColumnsByUserId(blog.getUserId())).map());
    }

    /**
     * 根据博客ID获取博客详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getBlogById(@PathVariable("id") String id){
        Blog blog = blogService.findBlogById(id);
        if (!redisUtils.hasKey("blogVisitors:"+id)){
            redisUtils.set("blogVisitors:"+id, String.valueOf(1));
        }else {
            redisUtils.incr("blogVisitors:"+id, 1L);
        }
        if (blog != null){
            blog.setVisitors(Integer.valueOf(redisUtils.get("blogVisitors:"+id)));
            System.out.println(projectService.getProjectByBlogId(id));
            blog.setProject(projectService.getProjectByBlogId(id));
            blog.setTags(tagService.getTagsbyBlogId(id));
            return Result.success(blog);
        }else {
            return Result.success(null);
        }
    }

    /**
     * 根据用户ID分页获取博客
     * @param page
     * @param limit
     * @param userId
     * @return
     */
    @GetMapping("/getpagesbyuserid/{userId}")
    public Result getPages(@RequestParam(value="page",defaultValue="1", required = false)Integer page,
                           @RequestParam(value = "limit", defaultValue = "7", required = false) Integer limit,@PathVariable("userId") String userId){
        PageHelper.startPage(page, limit);
        List<Blog> blogList = blogService.findBlogsByUserId(userId);
        //分页信息
        PageInfo pageInfo = new PageInfo(blogList);
        return Result.success(MapUtil.builder()
                .put("blogList", blogList)
                .put("total", pageInfo.getPages()).map());
    }

    /**
     * 根据分栏ID获取博客分页
     */
    @GetMapping("/getpagesbycolumnid/{columnId}")
    public Result getPagesByColumnId(@PathVariable("columnId") String columnId,
                                     @RequestParam(value = "limit", required = false, defaultValue = "6") Integer limit,
                                     @RequestParam(value = "page", required = false, defaultValue = "1") Integer page){
        log.info("获取专栏Id为{}的第{}页数据", columnId, page);
        PageHelper.startPage(page, limit);
        List<Blog> blogList = blogService.getPagesByColumnId(columnId);
        blogList.forEach((blog -> blog.setProject(projectService.getProjectByBlogId(blog.getId()))));
        PageInfo<Blog> pageInfo = new PageInfo<>(blogList);
        return Result.success(MapUtil.builder()
                .put("blogList", blogList)
                .put("total", pageInfo.getPages()).map());
    }


    @GetMapping("/search/{query}")
    public Result searchBlog(@PathVariable("query") String query){
        esBlogService.searchBlog(query);
        return Result.success(null);
    }
}
