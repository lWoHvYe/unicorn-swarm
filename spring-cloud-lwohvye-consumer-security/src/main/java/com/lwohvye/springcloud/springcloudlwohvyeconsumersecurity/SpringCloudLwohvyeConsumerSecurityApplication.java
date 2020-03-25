package com.lwohvye.springcloud.springcloudlwohvyeconsumersecurity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.lwohvye.springcloud.springcloudlwohvyeconsumersecurity.dao"})
public class SpringCloudLwohvyeConsumerSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudLwohvyeConsumerSecurityApplication.class, args);
	}

}
