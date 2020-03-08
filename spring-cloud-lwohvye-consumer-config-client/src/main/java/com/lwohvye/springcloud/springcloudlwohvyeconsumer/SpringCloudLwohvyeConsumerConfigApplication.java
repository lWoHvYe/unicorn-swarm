package com.lwohvye.springcloud.springcloudlwohvyeconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

//由于未使用数据源，需声明相关
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//开启eureka支持
@EnableEurekaClient//开启eureka支持
//开启feign支持
@EnableFeignClients(basePackages = {"com.lwohvye.springcloud.springcloudlwohvyeapi"})
//关于服务降级，需要对fallbackFactory相关类进行扫描，值为实现的fallbackFactory类的包路径
// 由于该配置会覆盖@SpringBootApplication注解的扫描，所以还需将该启动类所在的包加入进去
@ComponentScan(value = {
//        启动类所在包路径
        "com.lwohvye.springcloud.springcloudlwohvyeconsumer",
//        fallbackFactory实现所在包路径
        "com.lwohvye.springcloud.springcloudlwohvyeapi.service"}
)
public class SpringCloudLwohvyeConsumerConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudLwohvyeConsumerConfigApplication.class, args);
    }

}
