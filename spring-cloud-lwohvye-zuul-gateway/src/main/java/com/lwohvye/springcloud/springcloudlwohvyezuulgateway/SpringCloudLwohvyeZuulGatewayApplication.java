package com.lwohvye.springcloud.springcloudlwohvyezuulgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
//开启路由网关支持
@EnableZuulProxy
public class SpringCloudLwohvyeZuulGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudLwohvyeZuulGatewayApplication.class, args);
    }

}
