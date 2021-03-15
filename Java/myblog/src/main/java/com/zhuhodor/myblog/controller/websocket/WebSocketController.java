package com.zhuhodor.myblog.controller.websocket;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.tencentcloudapi.hcm.v20181106.models.EvaluationRequest;
import com.zhuhodor.myblog.Entity.Project;
import com.zhuhodor.myblog.Entity.User;
import com.zhuhodor.myblog.controller.BaseController;
import com.zhuhodor.myblog.service.ProjectService;
import com.zhuhodor.myblog.service.UserService;
import com.zhuhodor.myblog.vo.RequestVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * project申请
 * websocket服务器
 */
@Slf4j
@ServerEndpoint(value = "/pro/{userId}")
@Component
public class WebSocketController{
    private static ProjectService projectService;
    private static UserService userService;

    @Autowired
    public void setProjectService(ProjectService projectService){
        WebSocketController.projectService = projectService;
    }
    @Autowired
    public void setUserService(UserService userService){
        WebSocketController.userService = userService;
    }

    /** 记录当前在线连接数 */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    /** 存放所有在线的客户端 */
    private static Map<String, Session> clients = new ConcurrentHashMap<>();

    /** 当前连接的用户 */
    private String userId;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId,  Session session) {
        this.userId = userId;
        if (!clients.keySet().contains(userId)){
            onlineCount.incrementAndGet(); // 在线数加1
            clients.put(userId, session);
            log.info("有新连接加入：{}，当前连接为：{}", userId, onlineCount);
            List<RequestVo> requests = projectService.findRequestsByUserId(userId);
            this.sendMessage(userId, JSONUtil.toJsonStr(requests));
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        onlineCount.decrementAndGet(); // 在线数减1
        clients.remove(userId);
        log.info("有一连接关闭：{}，当前在线人数为：{}", userId, onlineCount.get());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     *            客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("服务端收到客户端[{}]的消息[{}]", userId, message);
        Project project = projectService.getOne(new QueryWrapper<Project>().eq("id", message).select("id", "start_user", "project_name"));
        User requestUser = userService.getOne(new QueryWrapper<User>().eq("id", userId).select("id", "username"));
        RequestVo vo = new RequestVo(project.getId(), requestUser.getId(), requestUser.getUsername(), project.getProjectName());
        sendMessage(String.valueOf(project.getStartUser()), JSONUtil.toJsonStr(vo));
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给客户端
     */
    public static void sendMessage(String userId, String message) {
        try {
            log.info("服务端给客户端[{}]发送消息[{}]", userId, message);
            if (clients.keySet().contains(userId)){
                clients.get(userId).getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败：{}", e);
        }
    }

}
