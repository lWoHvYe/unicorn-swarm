package com.lwohvye.springcloud.springcloudlwohvyeconfig3344;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
//开启配置中心
@EnableConfigServer
public class SpringCloudLwohvyeConfig3344Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudLwohvyeConfig3344Application.class, args);
    }

}
