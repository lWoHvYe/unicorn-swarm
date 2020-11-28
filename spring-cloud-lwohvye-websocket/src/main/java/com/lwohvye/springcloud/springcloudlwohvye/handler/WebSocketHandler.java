package com.lwohvye.springcloud.springcloudlwohvye.handler;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHandler extends AbstractWebSocketHandler {
    /**
     *  存储sessionId和webSocketSession
     *  需要注意的是，webSocketSession没有提供无参构造，不能进行序列化，也就不能通过redis存储
     *  在分布式系统中，要想别的办法实现webSocketSession共享
     */
    private static Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    private static Map<String, String> userMap = new ConcurrentHashMap<>();

    /**
     * webSocket连接创建后调用
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // 获取参数
        String user = String.valueOf(session.getAttributes().get("user"));
        userMap.put(user, session.getId());
        sessionMap.put(session.getId(), session);
    }

    /**
     * 接收到消息会调用
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(message.getPayload().toString());
        String content = jsonObject.getString("content");
        String targetAdminId = jsonObject.getString("targetId");
        if("0".equals(targetAdminId)){
            //  推送给所有人
            userMap.forEach((key,value)->{
                try {
                    this.sendMessage(key,content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }else{
            sendMessage("1", content);
        }
    }

    /**
     * 连接出错会调用
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        sessionMap.remove(session.getId());
    }

    /**
     * 连接关闭会调用
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionMap.remove(session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 后端发送消息
     */
    public void sendMessage(String user, String message) throws IOException {
        String sessionId = userMap.get(user);
        if (StringUtils.isEmpty(sessionId)) {
            return;
        }
        WebSocketSession session = sessionMap.get(sessionId);
        if (session == null) {
            return;
        }
        session.sendMessage(new TextMessage(message));
    }
}
