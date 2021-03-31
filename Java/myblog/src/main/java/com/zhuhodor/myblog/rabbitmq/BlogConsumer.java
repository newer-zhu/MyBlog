package com.zhuhodor.myblog.rabbitmq;

import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.Entity.Project;
import com.zhuhodor.myblog.elasticsearch.Entity.EsBlog;
import com.zhuhodor.myblog.elasticsearch.Entity.EsProject;
import com.zhuhodor.myblog.elasticsearch.Service.EsBlogRepository;
import com.zhuhodor.myblog.elasticsearch.Service.EsProjectRepository;
import com.zhuhodor.myblog.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BlogConsumer {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    EsBlogRepository esBlogRepository;
    @Autowired
    EsProjectRepository esProjectRepository;
    @Autowired
    RedisUtils redisUtils;

//    @RabbitListener(queuesToDeclare = @Queue(value = "worker"))
//    public void workerReceive(String message){
//        System.out.println(message);
//    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue, exchange = @Exchange(value = "blog", type = "topic"), key = "blog.del")
    })
    public void delEsBlog(Message message){
        EsBlog esBlog = (EsBlog) rabbitTemplate.getMessageConverter().fromMessage(message);
        log.info("es删除id == {}", esBlog.getId());
        esBlogRepository.delete(esBlog);
        redisUtils.del("blogVisitors:"+esBlog.getId());
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue, exchange = @Exchange(value = "blog", type = "topic"), key = "blog.upgrade")
    })
    public void esBlogUpgrade(Message message){
        Blog blog =(Blog) rabbitTemplate.getMessageConverter().fromMessage(message);
        log.info("更新blog:{}", blog.getTitle());
        EsBlog esBlog = new EsBlog();
        BeanUtils.copyProperties(blog, esBlog);
        esBlogRepository.save(esBlog);
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue, exchange = @Exchange(value = "blog", type = "topic"), key = "blog.save")
    })
    public void esSaveBlog(Message message){
        Blog blog =(Blog) rabbitTemplate.getMessageConverter().fromMessage(message);
        log.info("ES存入博客{}", blog.getTitle());
        EsBlog esBlog = new EsBlog();
        BeanUtils.copyProperties(blog, esBlog);
        esBlogRepository.save(esBlog);
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue, exchange = @Exchange(value = "project", type = "topic"), key = "project.save")
    })
    public void esSaveProject(Message message){
        Project project = (Project) rabbitTemplate.getMessageConverter().fromMessage(message);
        EsProject esProject = new EsProject();
        BeanUtils.copyProperties(project, esProject);
        esProjectRepository.save(esProject);
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue, exchange = @Exchange(value = "project", type = "topic"), key = "project.del")
    })
    public void delEsProject(String message){
        EsProject esProject = new EsProject(Integer.parseInt(message));
        esProjectRepository.delete(esProject);
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue, exchange = @Exchange(value = "project", type = "topic"), key = "project.upgrade")
    })
    public void upgradeEsProject(Message message){
        Project project = (Project) rabbitTemplate.getMessageConverter().fromMessage(message);
        EsProject esProject = new EsProject();
        BeanUtils.copyProperties(project, esProject);
        esProjectRepository.save(esProject);
    }
}
