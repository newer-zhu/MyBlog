package com.zhuhodor.myblog.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * rabbitMq工具类
 */
public class RabbitmqUtils {
    private static ConnectionFactory connectionFactory;
    static {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.247.124");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/blog");
        connectionFactory.setUsername("zhu");
        connectionFactory.setPassword("20001030");
    }

    /**
     * 获取连接
     * @return
     */
    public static Connection getConnection(){
        try {
            return connectionFactory.newConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }return null;
    }

    /**
     * 关闭
     * @param channel
     * @param con
     */
    public static void closeConnectionAndChanel(Channel channel, Connection con){
        try {
            if (channel != null || con != null){
                channel.close();
                con.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
