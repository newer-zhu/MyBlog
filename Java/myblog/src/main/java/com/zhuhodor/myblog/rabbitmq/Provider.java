package com.zhuhodor.myblog.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.zhuhodor.myblog.util.RabbitmqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Provider {
    public void sendMsg() throws IOException, TimeoutException {
        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();
        //a0：不存在字段创建 a1：持久化 a2: 是否独占队列 a3： 是否自动删除队列
        channel.queueDeclare("blog_1",false,false,false,null);
        //a0：交换机 a1：队列名称 a2：传递消息额外设置 a3：消息的具体内容
        channel.basicPublish("","blog_1", null, "msg".getBytes());
        RabbitmqUtils.closeConnectionAndChanel(channel, connection);
    }
}
