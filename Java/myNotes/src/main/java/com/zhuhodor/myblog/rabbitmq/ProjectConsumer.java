package com.zhuhodor.myblog.rabbitmq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhuhodor.myblog.Entity.ProjectModule.Project;
import com.zhuhodor.myblog.Entity.User;
import com.zhuhodor.myblog.elasticsearch.Entity.EsBlog;
import com.zhuhodor.myblog.elasticsearch.Entity.EsProject;
import com.zhuhodor.myblog.elasticsearch.Service.EsProjectRepository;
import com.zhuhodor.myblog.service.UserService;
import com.zhuhodor.myblog.util.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProjectConsumer {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    MailUtil mailUtil;
    @Autowired
    UserService userService;
    @Autowired
    EsProjectRepository esProjectRepository;

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

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue, exchange = @Exchange(value = "project", type = "topic"), key = "project.sendmail")
    })
    public void sendProjectMail(Message message){
        String mes = (String)rabbitTemplate.getMessageConverter().fromMessage(message);
        log.info("项目队列消费者接受的信息{}",mes);
        String[] split = mes.split(":");
        String userId = split[1];
        String projectId = split[0];
        User sender = userService.getOne(new QueryWrapper<User>().eq("id", userId).select("username"));
        User toUser = userService.findUserByProjectId(projectId);
        mailUtil.send(toUser.getEmail(), sender.getUsername());
    }
}
