package com.xxxx.server.controller;

import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.ChatMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

/**
 * @author: gouzi
 * @create: 2021-08-14 16:37
 **/
@Controller
public class WSController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 接收websocket请求
     * @param authentication 发送该websocket请求的用户，通过连接时的accessor.setUser(authentication)设置
     * @param chatMsg 消息内容
     */
    @MessageMapping("/ws/chat")
    public void handleMsg(Authentication authentication, ChatMsg chatMsg) {
        Admin admin = (Admin)authentication.getPrincipal();
        chatMsg.setFrom(admin.getUsername());
        chatMsg.setFormNickName(admin.getName());
        chatMsg.setDate(LocalDateTime.now());

        // 发送到队列
        simpMessagingTemplate.convertAndSendToUser(chatMsg.getTo(), "/queue/chat", chatMsg);
    }


}
