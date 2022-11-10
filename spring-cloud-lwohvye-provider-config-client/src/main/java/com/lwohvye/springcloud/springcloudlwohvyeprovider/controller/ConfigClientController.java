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
// @PropertySource("classpath:cloud-lwohvye-extra-config.yml") 使用config server不要用这个，如果这个是从server获取的，会报fileNotFound，不加也能读到，本质为client从远程获取所有所需的配置（可能是不同的文件），然后bind
@PropertySource(value = {"classpath:cloud-lwohvye-extra-config.yml", "classpath:extra-part-2-config.properties", "https://github.com/lWoHvYe/spring-cloud-lwohvye-config/blob/master/remoteFile.properties"}, ignoreResourceNotFound = false)
// 当本地配置文件与远程ControlConfig同名时，本地的文件会直接被ignore（不是覆盖，而是直接忽略整个文件）,当配置项在本地和远程都配置时，远程覆盖本地的。@PropertySource指定的不支持refresh （与远程同名的是refresh远程的update）
// 基于@RefreshScope，感觉上面的问题可以解决，只是没啥必要，有ControlConfig了，直接从那边取就行了
public class ConfigClientController {

    @Value("${config.info}")
    private String configInfo;

    @Value("${config.des:ignore}")
    private String configDes;

    @Value("${config.extra.des}")
    private String configExtraDes;

    @Value("${config.remote:ignore}")
    private String configRemote;

    @GetMapping("/configInfo")
    public String getConfigInfo() {
        return String.format(" %s -- %s -- %s ++ %s", configInfo, configDes, configExtraDes, configRemote);
    }
}
