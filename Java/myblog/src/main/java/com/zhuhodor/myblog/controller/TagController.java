package com.zhuhodor.myblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhuhodor.myblog.Entity.Tag;
import com.zhuhodor.myblog.common.Result;
import com.zhuhodor.myblog.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
@Slf4j
public class TagController {
    @Autowired
    TagService tagService;

    @GetMapping("/getbyblogid/{blogId}")
    public Result<List<Tag>> getTagsByBlogId(@PathVariable("blogId") String blogId){
        List<Tag> tags = tagService.getTagsbyBlogId(blogId);
        return Result.success(tags);
    }

    @PostMapping("/blogtotags/{blogId}")
    public Result blogToTags(@PathVariable("blogId") String blogId,
                             @RequestBody List<Tag> tags){
        log.info("将博客{}归纳进标签", blogId);
        //解除先前blog和tag的联系
        tagService.removeBlogsInTag(blogId);
        for (Tag t : tags){
            //将数据库里不存在的tag存入
            QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tag_name", t.getTagName());
            Tag target = tagService.getOne(queryWrapper);
            if (target == null){
                tagService.save(t);
                //重新建立blog与tag的联系
                tagService.blogIntoTags(blogId, String.valueOf(t.getId()));
            }else {
                //重新建立blog与tag的联系,前端自定义的tag是无ID的
                tagService.blogIntoTags(blogId, String.valueOf(target.getId()));
            }
        }
        return Result.success("添加标签成功");
    }
}
