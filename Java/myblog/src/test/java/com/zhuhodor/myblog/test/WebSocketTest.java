package com.zhuhodor.myblog.test;

import com.zhuhodor.myblog.controller.websocket.WebSocketController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WebSocketTest {

    @Test
    public void test(){
        WebSocketController.sendMessage("1", "hello bitch");
    }
}
