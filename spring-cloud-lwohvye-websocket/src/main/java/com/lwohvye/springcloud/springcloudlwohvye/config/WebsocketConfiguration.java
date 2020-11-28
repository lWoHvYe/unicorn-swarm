package com.lwohvye.springcloud.springcloudlwohvye.config;

import com.lwohvye.springcloud.springcloudlwohvye.handler.WebSocketHandler;
import com.lwohvye.springcloud.springcloudlwohvye.inteceptor.WebSocketInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebsocketConfiguration implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // webSocket通道
        // 指定处理器和路径
        registry.addHandler(new WebSocketHandler(), "/websocket")
                // 指定自定义拦截器
                .addInterceptors(new WebSocketInterceptor())
                // 允许跨域
                .setAllowedOrigins("*");
        // sockJs通道
        registry.addHandler(new WebSocketHandler(), "/sock-js")
                .addInterceptors(new WebSocketInterceptor())
                .setAllowedOrigins("*")
                // 开启sockJs支持
                .withSockJS();
    }
}
