package com.lwohvye.springcloud.springcloudlwohvyeconsumer.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


/**
 * @author Hongyan Wang
 * @packageName com.lwohvye.springcloud.springcloudlwohvyeprovider.controller
 * @className Cnarea2018Controller
 * @description
 * @date 2020/1/16 15:12
 */
@RequestMapping("/consumer/cnarea")
@RestController
public class Cnarea2018Controller_Consumer {

    private static final String REST_URL_PREFIX = "http://LWOHVYE-PROVIDER";

    /**
     * 使用 使用restTemplate访问restful接口非常的简单
     * (url, requestMap,ResponseBean.class)这三个参数分别代表 REST请求地址、请求参数、HTTP响应转换被转换成的对象类型。
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * @return String
     * @description 异步测试方法，province为使用逗号分隔的两位的省级区划名
     * @params [province, page, pageSize]
     * @author Hongyan Wang
     * @date 2020/1/16 22:13
     */
    @ApiOperation(value = "根据省名获取下属区划,多线程异步", notes = "多个省名使用逗号分隔")
    @ApiImplicitParam(name = "levels", value = "查询区划级别，实际可使用下拉选择 0省、直辖市 1市 2区县 3街道办事处 4社区居委会")
    @PostMapping("/list")
    public String list(String province, String levels, int page, int pageSize) {
        String url = REST_URL_PREFIX + "/cnarea/list";
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("province", province);
        builder.part("levels", levels);
        builder.part("page", page);
        builder.part("pageSize", pageSize);
        MultiValueMap<String, HttpEntity<?>> build = builder.build();
        return restTemplate.postForObject(url, build, String.class);
    }

    /**
     * @return String
     * @description 单线程模式，用于同多线程对比
     * @params [province, levels, page, pageSize]
     * @author Hongyan Wang
     * @date 2020/2/11 8:50
     */
    @ApiOperation(value = "根据省名获取下属区划,单线程模式", notes = "多个省名使用逗号分隔")
    @ApiImplicitParam(name = "levels", value = "查询区划级别，实际可使用下拉选择 0省、直辖市 1市 2区县 3街道办事处 4社区居委会")
    @PostMapping("/listSingle")
    public String listSingle(String province, String levels, int page, int pageSize) {
        String url = REST_URL_PREFIX + "/cnarea/listSingle";
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("province", province);
        builder.part("levels", levels);
        builder.part("page", page);
        builder.part("pageSize", pageSize);
        MultiValueMap<String, HttpEntity<?>> build = builder.build();
        return restTemplate.postForObject(url, build, String.class);
    }
}
