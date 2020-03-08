package com.lwohvye.springcloud.springcloudlwohvyeprovider.controller;

import cn.hutool.core.convert.Convert;
import com.github.pagehelper.PageInfo;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.Cnarea2018;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.common.annotation.LogAnno;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.common.util.BloomFilterHelper;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.common.util.RedisUtil;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.service.Cnarea2018Service;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author Hongyan Wang
 * @packageName com.lwohvye.springcloud.springcloudlwohvyeprovider.controller
 * @className Cnarea2018Controller
 * @description
 * @date 2020/1/16 15:12
 */
@RequestMapping("/cnarea")
@RestController
public class Cnarea2018Controller {

    @Autowired
    private Cnarea2018Service cnarea2018Service;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private BloomFilterHelper<String> bloomFilterHelper;
//    服务发现
    @Autowired
    private DiscoveryClient client;

    /**
     * @return com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel<java.util.List < com.github.pagehelper.PageInfo < com.lwohvye.springboot.dubbointerface.entity.Cnarea2018>>>
     * @description 异步测试方法，province为使用逗号分隔的两位的省级区划名
     * @params [province, page, pageSize]
     * @author Hongyan Wang
     * @date 2020/1/16 22:13
     */
    @LogAnno(operateType = "异步线程测试")
    @ApiOperation(value = "根据省名获取下属区划,多线程异步", notes = "多个省名使用逗号分隔")
    @ApiImplicitParam(name = "levels", value = "查询区划级别，实际可使用下拉选择 0省、直辖市 1市 2区县 3街道办事处 4社区居委会")
    @PostMapping("/list")
    public ResultModel<List<PageInfo<Cnarea2018>>> list(@RequestParam(value = "province") String province,
                                                        @RequestParam(value = "levels") String levels,
                                                        @RequestParam(value = "page", defaultValue = "1") String page,
                                                        @RequestParam(value = "pageSize", defaultValue = "10") String pageSize) {
//        设置区划级别，0省、直辖市 1市 2区县 3街道办事处 4社区居委会
        Integer level = Convert.toInt(levels);
        Integer pages = Convert.toInt(page);
        Integer pageSizes = Convert.toInt(pageSize);
//      切割字符串
        var completableFutureList = Arrays.stream(province.split(","))
//                使用布隆过滤器过滤掉不符合条件的省名
                .filter(pro -> redisUtil.includeByBloomFilter(bloomFilterHelper, "cnareaPro", pro))
//                用map后获取一个新的流，可以继续操作，用foreach后流便没了
                .map(name -> cnarea2018Service.list(name, level, pages, pageSizes))
                .collect(Collectors.toList());
        return new ResultModel<>(
                completableFutureList.stream()
//                将任务放入队列，等待结束
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
        );
    }

    /**
     * @return com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel<java.util.List < com.github.pagehelper.PageInfo < com.lwohvye.springboot.dubbointerface.entity.Cnarea2018>>>
     * @description 单线程模式，用于同多线程对比
     * @params [province, levels, page, pageSize]
     * @author Hongyan Wang
     * @date 2020/2/11 8:50
     */
    @LogAnno(operateType = "同步线程测试，与多线程对比")
    @ApiOperation(value = "根据省名获取下属区划,单线程模式", notes = "多个省名使用逗号分隔")
    @ApiImplicitParam(name = "levels", value = "查询区划级别，实际可使用下拉选择 0省、直辖市 1市 2区县 3街道办事处 4社区居委会")
    @PostMapping("/listSingle")
    public ResultModel<List<PageInfo<Cnarea2018>>> listSingle(String province, String levels, int page, int pageSize) {
//        设置区划级别，0省、直辖市 1市 2区县 3街道办事处 4社区居委会
        Integer level = Convert.toInt(levels);
//      切割字符串
        List<String> proList = Arrays.stream(province.split(","))
//                使用布隆过滤器过滤掉不符合条件的省名
                .filter(pro -> redisUtil.includeByBloomFilter(bloomFilterHelper, "cnareaPro", pro))
                .collect(Collectors.toList());
        return new ResultModel<>(cnarea2018Service.listSingle(proList, level, page, pageSize));
    }

    @GetMapping("/discovery")
    public Object discovery(){
        List<String> clientServices = client.getServices();
        System.out.println(clientServices);

        List<ServiceInstance> serviceInstances = client.getInstances("LWOHVYE-PROVIDER");
        System.out.println(serviceInstances);

        return this.client;
    }
}
