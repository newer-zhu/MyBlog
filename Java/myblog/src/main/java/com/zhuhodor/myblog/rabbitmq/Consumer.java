package com.zhuhodor.myblog.rabbitmq;

import com.rabbitmq.client.*;
import com.zhuhodor.myblog.util.RabbitmqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public void consumer() throws IOException, TimeoutException {
        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("blog_1",false,false,false,null);
//        channel.basicPublish("","blog_1", null, "msg".getBytes());
        channel.basicQos(1);//一次消费一个消息
        //a0：通道名称 a1：自动确认机制,关闭后可以保证消息不会丢失 a2：回调接口
        channel.basicConsume("blog_1", false, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(body.toString());
                //手动确认消息标识，每次确认一个
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
