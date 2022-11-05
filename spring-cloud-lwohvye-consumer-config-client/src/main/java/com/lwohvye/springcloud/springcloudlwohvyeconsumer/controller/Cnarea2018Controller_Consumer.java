package com.lwohvye.springcloud.springcloudlwohvyeconsumer.controller;

import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.Cnarea2018;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel;
import com.lwohvye.springcloud.springcloudlwohvyeapi.service.Cnarea2018FeignClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class Cnarea2018Controller_Consumer {


    @Autowired
    private Cnarea2018FeignClientService cnarea2018FeignClientService;

    @PostMapping("/consumer/cnarea/list")
    public ResultModel<List<Cnarea2018>> list() {
        return cnarea2018FeignClientService.list();
    }

}
