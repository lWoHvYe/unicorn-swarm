package com.lwohvye.springcloud.springcloudlwohvyeapi.api;

import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.Cnarea2018;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author Hongyan Wang
 * @packageName com.lwohvye.springcloud.springcloudlwohvyeprovider.controller
 * @className Cnarea2018Controller
 * @description
 * @date 2020/1/16 15:12
 */
public interface Cnarea2018API {


    @PostMapping("/cnarea/list")
    ResultModel<List<Cnarea2018>> list();

}
