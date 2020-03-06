package com.lwohvye.springcloud.springcloudlwohvyeprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//开启缓存支持
@EnableCaching
//开启定时任务
@EnableScheduling
@EnableEurekaClient //本服务启动后会自动注册进eureka服务中
@EnableDiscoveryClient //服务发现
public class SpringCloudLwohvyeProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudLwohvyeProviderApplication.class, args);
    }

}
