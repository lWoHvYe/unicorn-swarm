package com.lwohvye.springcloud.springcloudlwohvyeconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

//由于未使用数据源，需声明相关
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//开启监控
@EnableHystrixDashboard
public class SpringCloudLwohvyeConsumerApplication_Hystrix {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudLwohvyeConsumerApplication_Hystrix.class, args);
    }

}
