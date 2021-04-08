package com.zhuhodor.myblog.controller;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhuhodor.myblog.AI.TextAnalysis;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.Entity.Tag;
import com.zhuhodor.myblog.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/tag")
@Slf4j
public class TagController extends BaseController{

    /**
     *获取标签
     * @param
     * @return
     */
    @PostMapping()
    public Result getTag(@RequestBody Tag tag){
        //根据Id查询
        if (tag.getId() != null){
            Tag one = tagService.getOne(new QueryWrapper<Tag>().eq("id", tag.getId()));
            return Result.success(one);
        }else {//根据名字查询
            Tag one = tagService.getOne(new QueryWrapper<Tag>().eq("tag_name", tag.getTagName()));
            if (one != null){
                return Result.success(one);
            }else {
                Tag noTag = new Tag();
                noTag.setTagName(tag.getTagName());
                tagService.save(noTag);
                return Result.success(noTag);
            }
        }
    }

    /**
     * 根据博客Id获取所有标签
     * @param blogId
     * @return
     */
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
        tagService.setBlog2Tags(blogId, tags);
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

    /**
     * 根据标签Id获取博客
     * @param tagId
     * @return
     */
    @GetMapping("/blogs/{tagId}")
    public Result getBlogsByTagId(@PathVariable("tagId") String tagId, @RequestParam("page") Integer page){
        log.info("获取标签={}的博客", tagId);
        PageHelper.startPage(page, 1);
        List<Blog> list = tagService.findBlogsByTagId(tagId);
        PageInfo<Blog> pageInfo = new PageInfo<>(list);
        return Result.success(MapUtil.builder()
                .put("list", list)
                .put("total", pageInfo.getPages()).map());
    }

    /**
     * 获取项目标签
     * @param projectId
     * @return
     */
    @GetMapping("/{projectId}")
    public Result getTagsByProjectId(@PathVariable("projectId") String projectId){
        log.info("获取项目{}的标签", projectId);
        HashMap<String, Integer> tagMap = new HashMap<>();
        List<Blog> blogs = projectService.findBlogsByProjectId(projectId);
        for (Blog b : blogs){
            List<Tag> tags = tagService.getTagsbyBlogId(b.getId());
            for (Tag t : tags){
                tagMap.put(t.getTagName(), tagMap.getOrDefault(t.getTagName(), 0)+1);
            }
        }
        ArrayList<String> res = new ArrayList<>();
        tagMap.forEach((k, v) -> {
            res.add(k+":"+v);
        });
        return Result.success(res);
    }

    /**
     * 标签关联
     * @param name
     * @return
     */
    @GetMapping("/relative/{name}")
    public Result relativeTags(@PathVariable String name){
        Object relation = TextAnalysis.wordRelation(name);
        return Result.success(relation);
    }
}
