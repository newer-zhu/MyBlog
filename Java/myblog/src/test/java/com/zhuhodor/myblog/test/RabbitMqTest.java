package com.zhuhodor.myblog.test;

import com.zhuhodor.myblog.rabbitmq.Consumer;
import com.zhuhodor.myblog.rabbitmq.Provider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootTest
public class RabbitMqTest {
    public static void main(String[] args) throws IOException, TimeoutException {
//        Provider provider = new Provider();
//        provider.sendMsg();
        Consumer consumer = new Consumer();
        consumer.consumer();
    }
    @Test
    public void test() throws IOException, TimeoutException {
        Provider provider = new Provider();
        provider.sendMsg();
    }
}
