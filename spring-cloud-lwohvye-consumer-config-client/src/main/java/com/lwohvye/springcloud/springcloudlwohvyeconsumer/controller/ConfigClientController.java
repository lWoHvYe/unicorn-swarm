package com.lwohvye.springcloud.springcloudlwohvyeconsumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * @version 1.0
 * @create 2020/03/06
 */
@RestController
@RefreshScope
public class ConfigClientController {

    @Autowired
    RestTemplate restTemplate;

    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/configInfo")
    public String getConfigInfo() {
        var provideInfo = restTemplate.exchange("http://LWOHVYE-PROVIDER/configInfo", HttpMethod.GET, null, String.class);
        return configInfo + provideInfo;
    }
}
