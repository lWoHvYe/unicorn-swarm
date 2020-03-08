package com.lwohvye.springcloud.springcloudlwohvyeprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//开启缓存支持
@EnableCaching
//开启定时任务
@EnableScheduling
@EnableEurekaClient //本服务启动后会自动注册进eureka服务中
@EnableDiscoveryClient //启用服务发现
//开启监控
@EnableHystrixDashboard
//指定实体类位置，解决启动报找不到实体类
@EntityScan(basePackages = {"com.lwohvye.springcloud.springcloudlwohvyeapi.entity"})
public class SpringCloudLwohvyeProviderConfigClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudLwohvyeProviderConfigClientApplication.class, args);
    }

}
