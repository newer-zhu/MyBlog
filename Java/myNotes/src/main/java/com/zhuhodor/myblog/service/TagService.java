package com.zhuhodor.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.Entity.Tag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService extends IService<Tag> {

    List<Tag> getTagsbyBlogId(String blogId);

    boolean removeBlogsInTag(String blogId);

    boolean blogIntoTags(String blogId, String tagId);

    List<Blog> findBlogsByTagId(String tagId);

    boolean setBlog2Tags(String blogId, List<String> tags);

    List<Integer> countBlogs(Integer id);
}
