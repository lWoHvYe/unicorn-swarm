package com.lwohvye.springcloud.springcloudlwohvyeconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

//由于未使用数据源，需声明相关
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient//开启eureka支持
@EnableFeignClients(basePackages = {"com.lwohvye.springcloud.springcloudlwohvyeapi"})//开启feign支持
public class SpringCloudLwohvyeConsumerApplication_Feign {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudLwohvyeConsumerApplication_Feign.class, args);
    }

}
