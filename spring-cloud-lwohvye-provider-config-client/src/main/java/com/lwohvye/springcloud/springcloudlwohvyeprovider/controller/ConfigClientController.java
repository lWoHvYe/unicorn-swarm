package com.lwohvye.springcloud.springcloudlwohvyeprovider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0
 * @create 2020/03/06
 */
@RestController
@RefreshScope
// @PropertySource("classpath:cloud-lwohvye-extra-config.yml") 使用config server不要用这个，如果这个是从server获取的，会报fileNotFound，不加也能读到
public class ConfigClientController {

    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/configInfo")
    public String getConfigInfo(){
        return configInfo;
    }
}
