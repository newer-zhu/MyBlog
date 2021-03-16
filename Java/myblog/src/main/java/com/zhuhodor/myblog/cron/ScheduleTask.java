package com.zhuhodor.myblog.cron;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.service.BlogService;
import com.zhuhodor.myblog.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleTask {
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    BlogService blogService;

    /**
     * 定时更新文章浏览数
     */
    @Scheduled(cron = "0 59 23 * * ?")
    public void saveVisitors(){
        List<Blog> id = blogService.list(new QueryWrapper<Blog>().select("id"));
        id.forEach((i) -> {
            String s = redisUtils.get("blogVisitors:" + i.getId());
            if (s != null){
                i.setVisitors(Integer.parseInt(s));
            }
        });
        blogService.updateBatchById(id);
    }

    /**
     * 定时清空热搜
     */
    @Scheduled(cron = "0 0 7 * * ?")
    public void removeKeywords(){
        redisUtils.zremAll("keywords");
    }
}
