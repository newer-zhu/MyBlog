package com.zhuhodor.myblog.controller.websocket;

import com.zhuhodor.myblog.Entity.websocketEntity.Message;
import com.zhuhodor.myblog.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
        import org.springframework.messaging.handler.annotation.SendTo;
        import org.springframework.stereotype.Controller;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

@Controller
@Slf4j
@ServerEndpoint("/timeline")
public class WebSocket {
    @OnOpen
    public Result onOpen(){
        return Result.success(200,"msg",null);
    }

    @OnMessage
    public void onMessage(String msg){

    }
}
