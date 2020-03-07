package com.lwohvye.springcloud.springcloudlwohvyeeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
//EurekaServer服务器端启动类,接受其它微服务注册进来
@EnableEurekaServer
public class SpringCloudLwohvyeEurekaApplication_7003 {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudLwohvyeEurekaApplication_7003.class, args);
    }

}
