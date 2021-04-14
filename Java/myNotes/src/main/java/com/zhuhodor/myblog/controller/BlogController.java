package com.zhuhodor.myblog.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.Entity.User;
import com.zhuhodor.myblog.common.Result;
import com.zhuhodor.myblog.elasticsearch.Entity.EsBlog;
import com.zhuhodor.myblog.vo.SearchRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.*;


@RestController
@RequestMapping("/blog")
@Slf4j
public class BlogController extends BaseController {

    /**
     * 根据博客ID删除博客
     *
     * @param blogId
     * @return
     */
    @GetMapping("/delblogbyid")
    public Result delBlogById(@RequestParam("blogId") String blogId) {
        log.info("用户删除了id为{}的博客", blogId);
        blogService.delBlog(blogId);
        rabbitTemplate.convertAndSend("blog", "blog.del", new EsBlog(blogId));
        return Result.success("删除成功");
    }

    /**
     * 根据用户ID获取博客
     *
     * @param userId
     * @return
     */
    @GetMapping("/getbyuserid/{userId}")
    public Result listBlogsByUserId(@PathVariable("userId") String userId) {
        log.info("查询用户博客");
        List<Blog> blogs = blogService.findBlogsByUserId(userId);
        return Result.success(blogs);
    }

    /**
     * 新建博客
     *
     * @param blog
     * @return
     */
    @PostMapping("/save")
    public Result saveBlog(@RequestBody @Validated Blog blog,
                           @RequestParam(value = "projectId", required = false, defaultValue = "") String projectId) {
        log.info("储存id为 {}的博客", blog.id);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        blog.setCreatedAt(now);
        blogService.saveBlog(blog, projectId);
        rabbitTemplate.convertAndSend("blog", "blog.save", blog);
        return Result.success(MapUtil.builder().put("blog", blog)
                .put("pushTags", keywordsExtraction.generate(blog.getContent()))
                .put("columns", columnService.getColumnsByUserId(blog.getUserId())).map());
    }

    /**
     * 修改博客
     *
     * @param blog
     * @return
     */
    @PostMapping("/modifyblog")
    public Result modifyBlog(@RequestBody @Validated Blog blog) {
        log.info("修改id为 {}的博客", blog.id);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        blog.setCreatedAt(now);
        blogService.editBlog(blog);
        rabbitTemplate.convertAndSend("blog", "blog.upgrade", blog);
        return Result.success(MapUtil.builder().put("blog", blog)
                .put("pushTags", keywordsExtraction.generate(blog.getContent()))
                .put("columns", columnService.getColumnsByUserId(blog.getUserId())).map());
    }

    /**
     * 根据博客ID获取博客详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getBlogById(@PathVariable("id") String id) {
        log.info("获取BlogId={}的博客", id);
        Blog blog = blogService.findBlogById(id);
        if (!redisUtils.hasKey("blogVisitors:" + id)) {
            redisUtils.set("blogVisitors:" + id, String.valueOf(1));
        } else {
            redisUtils.incr("blogVisitors:" + id, 1L);
        }
        if (blog != null) {
            //如果是文件
            if (blog.getIsFile() == 1) {
                String[] split = blog.getContent().split("\\\\");
                blog.setContent(split[split.length - 1]);
            }
            blog.setUser(userService.findUserById(Integer.parseInt(blog.getUserId())));
            blog.setVisitors(Integer.valueOf(redisUtils.get("blogVisitors:" + id)));
            blog.setProject(projectService.getProjectByBlogId(id));
            blog.setTags(tagService.getTagsbyBlogId(id));
            return Result.success(blog);
        } else {
            return Result.success(null);
        }
    }

    /**
     * 根据用户ID分页获取博客
     *
     * @param page
     * @param limit
     * @param userId
     * @return
     */
    @GetMapping("/getpagesbyuserid/{userId}")
    public Result getPages(@RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
                           @RequestParam(value = "limit", defaultValue = "7", required = false) Integer limit, @PathVariable("userId") String userId) {
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
                                     @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        log.info("获取专栏Id为{}的第{}页数据", columnId, page);
        PageHelper.startPage(page, limit);
        List<Blog> blogList = blogService.getPagesByColumnId(columnId);
        blogList.forEach((blog -> blog.setProject(projectService.getProjectByBlogId(blog.getId()))));
        PageInfo<Blog> pageInfo = new PageInfo<>(blogList);
        return Result.success(MapUtil.builder()
                .put("blogList", blogList)
                .put("total", pageInfo.getPages()).map());
    }

    /**
     * 相似文章
     */
    @PostMapping("/relative/{blogId}")
    public Result relative(@PathVariable String blogId, @RequestBody Integer[] tags) {
        HashSet<Blog> set = new HashSet<>();
//        StringBuilder sb = new StringBuilder();
//        if (tags.length != 0){
//            for (String t : tags) {
//                sb.append(t);
//            }
//        }else {
//            sb.append(blogService.getOne(new QueryWrapper<Blog>().eq("id", blogId).select("id, title")).getTitle());
//        }
//        SearchRequest request = new SearchRequest();
//        request.setQuerystr(sb.toString());
//        Map<String, Object> map = esBlogService.searchBlog(request);
//        List<Blog> blogList =(List<Blog>) map.get("blogList");
//        blogList.forEach(l -> {
//            if (l.getId() != blogId)
//                set.add(l);
//        });
        if (tags.length == 0){
            String title = blogService.getOne(new QueryWrapper<Blog>().eq("id", blogId).select("id, title")).getTitle();
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.setQuerystr(title).setCurrent(1);
            ArrayList<Blog> list = (ArrayList<Blog>)esBlogService.searchBlog(searchRequest).get("blogList");
            list.forEach(l -> {
                if (!l.getId().equals(blogId)){
                    set.add(l);
                }
            });
        }else {
            for (int t : tags) {
//            标签下的所有博客ID
                List<Integer> blogs = tagService.countBlogs(t);
                for (int i = 0; i < 3; i++) {
//                随便取出其中一条
                    Blog blog = blogService.getOne(
                            new QueryWrapper<Blog>().eq("id", blogs.get(new Random().nextInt(blogs.size())))
                                    .select("id", "title", "visitors", "is_file", "user_id", "created_at", "summary")
                    );
                    if (!blogId.equals(blog.getId())) {
                        blog.setUser(userService.getOne(new QueryWrapper<User>()
                                .eq("id", blog.getUserId()).select("id", "username")));
                        set.add(blog);
                    }
                }
            }
        }
        return Result.success(set);
    }
}
