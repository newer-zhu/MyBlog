package com.zhuhodor.myblog.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.service.BlogService;
import com.zhuhodor.myblog.service.ProjectService;
import com.zhuhodor.myblog.util.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
public class RedisTest {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisUtils redisUtils;
    @Autowired
    BlogService blogService;
    @Autowired
    ProjectService projectService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;


    @Test
    public void testRedis(){
        List<Blog> id = blogService.list(new QueryWrapper<Blog>().select("id"));
        id.forEach((i) -> {
            i.setVisitors(Integer.parseInt(redisUtils.get("blogVisitors:"+i.getId())));
        });
        blogService.updateBatchById(id);
    }

    @Test
    public void testTime(){
        List<Date> time = projectService.getBlogTime("1");
        HashMap<String, Integer> map = new HashMap<>();
        time.forEach(t -> {
            String format = new SimpleDateFormat("yyyy-MM-dd").format(t);
            map.put(format, map.getOrDefault(format, 0) + 1);
        });
        for (String m : map.keySet())
            System.out.println(m);
    }

}
