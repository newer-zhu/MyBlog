package com.zhuhodor.myblog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.Entity.Tag;
import com.zhuhodor.myblog.mapper.TagMapper;
import com.zhuhodor.myblog.service.TagService;
import com.zhuhodor.myblog.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@Component
@Slf4j
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Autowired
    TagMapper tagMapper;
    @Autowired
    RedisUtils redisUtils;


    @Override
    public List<Tag> getTagsbyBlogId(String blogId) {
        return tagMapper.getTagsbyBlogId(blogId);
    }

    @Override
    public boolean removeBlogsInTag(String blogId) {
        return tagMapper.removeBlogsInTag(blogId);
    }

    @Override
    public boolean blogIntoTags(String blogId, String tagId) {
        if (!redisUtils.hasKey("tag_:"+tagId)){
            redisUtils.hset("tag_"+tagId, "number",String.valueOf(1));
            redisUtils.hset("tag_"+tagId, "heat",String.valueOf(10));
        }else {
            redisUtils.hincrby("tag_"+tagId, "number", 1);
            redisUtils.hincrby("tag_"+tagId, "heat", 10);
        }
        return tagMapper.blogIntoTags(blogId, tagId);
    }

    @Override
    public List<Blog> findBlogsByTagId(String tagId) {
        return tagMapper.findBlogsByTagId(tagId);
    }

    @Override
    public boolean setBlog2Tags(String blogId, List<String> tags) {
        //解除先前blog和tag的联系
        removeBlogsInTag(blogId);
        for (String t : tags){
            //将数据库里不存在的tag存入
            Tag target = tagMapper.selectOne(new QueryWrapper<Tag>().eq("tag_name", t));
            //没有这个tag
            if (target == null){
                Tag tag = new Tag(t);
                tagMapper.insert(tag);
                //重新建立blog与tag的联系
                blogIntoTags(blogId, String.valueOf(tag.getId()));
            }else {
                //重新建立blog与tag的联系,前端自定义的tag是无ID的
                blogIntoTags(blogId, String.valueOf(target.getId()));
            }
        }
        return false;
    }
}
