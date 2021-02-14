package com.zhuhodor.myblog.test;

import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisTest {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Qualifier("blogRedisTemplate")
    @Autowired
    RedisTemplate blogRedisTemplate;

    @Test
    public void testRedis(){
        stringRedisTemplate.opsForValue().set("hello", "world");
        System.out.println(stringRedisTemplate.opsForValue().get("hello"));
    }

    @Test
    public void testBlogRedis(){
        blogRedisTemplate.opsForValue().set("blog-1",new Blog());
        System.out.println(blogRedisTemplate.opsForValue().get("blog-1"));
    }
}
