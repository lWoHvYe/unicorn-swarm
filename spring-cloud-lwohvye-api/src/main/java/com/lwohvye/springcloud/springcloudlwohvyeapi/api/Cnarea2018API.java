package com.lwohvye.springcloud.springcloudlwohvyeapi.api;

import com.github.pagehelper.PageInfo;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.Cnarea2018;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Hongyan Wang
 * @packageName com.lwohvye.springcloud.springcloudlwohvyeprovider.controller
 * @className Cnarea2018Controller
 * @description
 * @date 2020/1/16 15:12
 */
@RequestMapping("/cnarea")
public interface Cnarea2018API {


    @PostMapping("/list")
    ResultModel<List<PageInfo<Cnarea2018>>> list(@RequestParam(value = "province") String province,
                                                 @RequestParam(value = "levels") String levels,
                                                 @RequestParam(value = "page", defaultValue = "1") String page,
                                                 @RequestParam(value = "pageSize", defaultValue = "10") String pageSize);

    @PostMapping("/listSingle")
    ResultModel<List<PageInfo<Cnarea2018>>> listSingle(String province, String levels, int page, int pageSize);

}
