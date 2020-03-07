package com.lwohvye.springcloud.springcloudlwohvyeprovider.controller;

import com.lwohvye.springcloud.springcloudlwohvyeprovider.common.util.BloomFilterHelper;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.common.util.RedisUtil;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.service.Cnarea2018Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Hongyan Wang
 * @packageName com.lwohvye.springcloud.springcloudlwohvyeprovider.controller
 * @className BloomFilterController
 * @description
 * @date 2020/2/6 15:50
 */
@RestController
@RequestMapping("/bloomFilter")
//限定该类只有admin角色可以访问
public class BloomFilterController {

    @Autowired
    private Cnarea2018Service cnarea2018Service;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private BloomFilterHelper<String> bloomFilterHelper;

    /**
     * @description 将行政区划中的省名放入
     * @params []
     * @return java.lang.String
     * @author Hongyan Wang
     * @date 2020/2/6 17:50
     */
    @GetMapping("/addCnareaPro")
    public String addCnareaPro() {
        addCnarea();
        return "success";
    }

    /**
     * @description 定时任务，每天23点执行一次，更新欲过滤的内容，可以使用增量更新的方式，当前因为无更新字段，使用全量更新
     * @params []
     * @return void
     * @author Hongyan Wang
     * @date 2020/2/7 11:09
     */
    //TODO 方法不能为private的，这点需要特别注意
    @Scheduled(cron = "0 0 23 * * ?")
    public void addCnarea() {
//        首先删除对应缓存
        redisUtil.delete("cnareaPro");
//        再重新添加
        cnarea2018Service.listProName().forEach(pro -> redisUtil.addByBloomFilter(bloomFilterHelper, "cnareaPro", pro));
    }
}
