package com.zhuhodor.myblog.test;

import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootTest
public class RabbitMqTest {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void test() throws IOException, TimeoutException {
        rabbitTemplate.convertAndSend("hello", "hello world");
    }

    @Test
    public void testWorker(){
        rabbitTemplate.convertAndSend("worker", "hello worker");
    }

    @Test
    public void testFanout(){
        rabbitTemplate.convertAndSend("log","fanout", "hello fanout");
    }

    @Test
    public void testRoute(){
        rabbitTemplate.convertAndSend("exchange", "info", "message");
    }

    @Test
    public void testFan(){
        Blog blog = new Blog();
        blog.setTitle("666");
        rabbitTemplate.convertAndSend("blog", "blog.save", rabbitTemplate.getMessageConverter().toMessage(blog, new MessageProperties()));
    }
}
