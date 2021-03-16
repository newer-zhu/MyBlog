package com.zhuhodor.myblog.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.Entity.Tag;
import com.zhuhodor.myblog.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/tag")
@Slf4j
public class TagController extends BaseController{

    @GetMapping("/getbyblogid/{blogId}")
    public Result<List<Tag>> getTagsByBlogId(@PathVariable("blogId") String blogId){
        List<Tag> tags = tagService.getTagsbyBlogId(blogId);
        return Result.success(tags);
    }

    /**
     * 将博客归纳进标签
     * @param blogId
     * @param tags
     * @return
     */
    @PostMapping("/blogtotags/{blogId}")
    public Result blogToTags(@PathVariable("blogId") String blogId,
                             @RequestBody List<String> tags){
        log.info("将博客{}归纳进标签", blogId);
        //解除先前blog和tag的联系
        tagService.removeBlogsInTag(blogId);
        for (String t : tags){
            //将数据库里不存在的tag存入
            Tag target = tagService.getOne(new QueryWrapper<Tag>().eq("tag_name", t));
            //没有这个tag
            if (target == null){
                Tag tag = new Tag(t);
                tagService.save(tag);
                //重新建立blog与tag的联系
                tagService.blogIntoTags(blogId, String.valueOf(tag.getId()));
            }else {
                //重新建立blog与tag的联系,前端自定义的tag是无ID的
                tagService.blogIntoTags(blogId, String.valueOf(target.getId()));
            }
        }
        return Result.success("添加标签成功");
    }

    /**
     * 获取所有标签
     * @return
     */
    @GetMapping("/tags")
    public Result getTags(){
        List<Tag> tags = tagService.list();
        return Result.success(tags);
    }

    @GetMapping("/blogs/{tagId}")
    public Result getBlogs(@PathVariable("tagId") String tagId){
        return Result.success(tagService.findBlogsByTagId(tagId));
    }

    /**
     * 获取项目标签
     * @param projectId
     * @return
     */
    @GetMapping("/{projectId}")
    public Result getTagsByProjectId(@PathVariable("projectId") String projectId){
        log.info("获取项目{}的标签", projectId);
        HashMap<Tag, Integer> tagMap = new HashMap<>();
        List<Blog> blogs = projectService.findBlogsByProjectId(projectId);
        int total = 0;
        for (Blog b : blogs){
            List<Tag> tags = tagService.getTagsbyBlogId(b.getId());
            for (Tag t : tags){
                total++;
                tagMap.put(t, tagMap.getOrDefault(t, 0)+1);
            }
        }
        return Result.success(MapUtil.builder()
                .put("tags", tagMap)
                .put("total", total).map());
    }
}
