package com.lwohvye.springcloud.springcloudlwohvyeconsumer.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 前端控制器
 *
 * @author author
 * @since 2019-10-08
 */
@RestController
@RequestMapping("/consumer/mpCustom")
public class MpCustomController_Consumer {

    private static final String REST_URL_PREFIX = "http://LWOHVYE-PROVIDER";

    /**
     * 使用 使用restTemplate访问restful接口非常的简单
     * (url, requestMap,ResponseBean.class)这三个参数分别代表 REST请求地址、请求参数、HTTP响应转换被转换成的对象类型。
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * @return String
     * @description 获取列表
     * @params []
     * @author Hongyan Wang
     * @date 2019/10/9 15:52
     */
    @ApiOperation(value = "获取客户列表", notes = "获取客户列表，暂不提供分页及搜索")
    @GetMapping("/list")
    public String list() {
        String url = REST_URL_PREFIX + "/mpCustom/list";
        return restTemplate.getForObject(url, String.class);
    }

}
