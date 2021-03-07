package com.zhuhodor.myblog.controller.websocket;

import com.zhuhodor.myblog.Entity.websocket.Message;
import com.zhuhodor.myblog.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler;
import org.springframework.stereotype.Controller;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

@Controller
@Slf4j
public class WebSocketController {


    /**
     * 处理路径为/pro/contribute的消息
     * @param message
     * @return
     */
    @MessageMapping("/contribute")
    @SendTo("/topic/contributors")
    public Message tend2Join(Message message){
        return message;
    }

}
