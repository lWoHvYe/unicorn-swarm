package com.lwohvye.springcloud.springcloudlwohvyeprovider.controller;

import com.lwohvye.springcloud.springcloudlwohvyeapi.api.Cnarea2018API;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.Cnarea2018;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Hongyan Wang
 * @packageName com.lwohvye.springcloud.springcloudlwohvyeprovider.controller
 * @className Cnarea2018Controller
 * @description
 * @date 2020/1/16 15:12
 */
@RestController
public class Cnarea2018Controller implements Cnarea2018API {
    //    服务发现
    @Autowired
    private DiscoveryClient client;

    @Override
    public ResultModel<List<Cnarea2018>> list() {
        return new ResultModel<>();
    }

    @GetMapping("/discovery")
    public Object discovery() {
        List<String> clientServices = client.getServices();
        System.out.println(clientServices);

        List<ServiceInstance> serviceInstances = client.getInstances("LWOHVYE-PROVIDER");
        System.out.println(serviceInstances);

        return this.client;
    }
}
