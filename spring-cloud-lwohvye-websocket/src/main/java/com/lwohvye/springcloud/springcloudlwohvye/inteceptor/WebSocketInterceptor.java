package com.lwohvye.springcloud.springcloudlwohvye.inteceptor;

import com.lwohvye.springcloud.springcloudlwohvye.entiry.Constants;
import org.apache.shiro.SecurityUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;


public class WebSocketInterceptor extends HttpSessionHandshakeInterceptor {
    /**
     * handler处理前调用,attributes属性最终在WebSocketSession里,
     * 可能通过webSocketSession.getAttributes().get(key值)获得
     * 主体是获取userName用于消息推送
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        System.out.println("Before Handshake");
        /*
         * if (request instanceof ServletServerHttpRequest) {
         * ServletServerHttpRequest servletRequest = (ServletServerHttpRequest)
         * request; HttpSession session =
         * servletRequest.getServletRequest().getSession(false); if (session !=
         * null) { //使用userName区分WebSocketHandler，以便定向发送消息 String userName =
         * (String) session.getAttribute(Constants.SESSION_USERNAME); if
         * (userName==null) { userName="default-system"; }
         * attributes.put(Constants.WEBSOCKET_USERNAME,userName);
         *
         * } }
         */

        //使用userName区分WebSocketHandler，以便定向发送消息(使用shiro获取session,或是使用上面的方式)
        String userName = (String) SecurityUtils.getSubject().getSession().getAttribute(Constants.SESSION_USERNAME);
        if (userName == null) {
            userName = "default-system";
        }
        attributes.put(Constants.WEBSOCKET_USERNAME, userName);

        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
        System.out.println("After Handshake");
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
