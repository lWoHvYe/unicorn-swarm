package com.lwohvye.springcloud.springcloudlwohvyeprovider.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.Cnarea2018;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.dao.master.MasterCnarea2018Mapper;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.dao.slave.SlaveCnarea2018Mapper;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.service.Cnarea2018Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

//这里的@Service注解使用dubbo的，需注意
@Service
@Slf4j
public class Cnarea2018ServiceImpl implements Cnarea2018Service {

    @Autowired
    private MasterCnarea2018Mapper masterCnarea2018Mapper;
    @Autowired
    private SlaveCnarea2018Mapper slaveCnarea2018Mapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return masterCnarea2018Mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Cnarea2018 record) {
        return masterCnarea2018Mapper.insert(record);
    }

    @Override
    public int insertSelective(Cnarea2018 record) {
        return masterCnarea2018Mapper.insertSelective(record);
    }

    @Override
    public Cnarea2018 selectByPrimaryKey(Integer id) {
        return slaveCnarea2018Mapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Cnarea2018 record) {
        return masterCnarea2018Mapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Cnarea2018 record) {
        return masterCnarea2018Mapper.updateByPrimaryKey(record);
    }

    //    标识异步方法
    @Async
    @Override
    public CompletableFuture<PageInfo<Cnarea2018>> list(String province, Integer level, int page, int pageSize) {
        log.warn(Thread.currentThread().getName() + " start this task!");
        Cnarea2018 cnarea2018 = new Cnarea2018();
        cnarea2018.setMergerName(province);
        cnarea2018.setLevel(level);
//        当使用索引时，不要使用order by非查询的索引字段，即当查询条件与排序使用不同的字段时，即使两字段都建立索引，也会导致索引失效
        PageInfo<Cnarea2018> pageInfo = PageHelper.startPage(page, pageSize)
                .doSelectPageInfo(() -> slaveCnarea2018Mapper.selectByAll(cnarea2018));
//        暂停一会，使异步更明显
//        try {
//            Thread.sleep(10000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        log.warn(Thread.currentThread().getName() + " finish this task!!");
        //返回一个已经用给定值完成的新的CompletableFuture。
        return CompletableFuture.completedFuture(pageInfo);

    }

    /**
     * @return java.util.List<java.lang.String>
     * @description 获取省名列表，用于布隆过滤
     * @params []
     * @author Hongyan Wang
     * @date 2020/2/6 15:57
     */
    @Override
    public List<String> listProName() {
        return slaveCnarea2018Mapper.listProName();
    }

    @Override
    public List<PageInfo<Cnarea2018>> listSingle(List<String> proList, Integer level, int page, int pageSize) {
        List<PageInfo<Cnarea2018>> list = new ArrayList<>();
        for (String province : proList) {
            Cnarea2018 cnarea2018 = new Cnarea2018();
            cnarea2018.setMergerName(province);
            cnarea2018.setLevel(level);
//        当使用索引时，不要使用order by非查询的索引字段，即当查询条件与排序使用不同的字段时，即使两字段都建立索引，也会导致索引失效
            PageInfo<Cnarea2018> pageInfo = PageHelper.startPage(page, pageSize)
                    .doSelectPageInfo(() -> slaveCnarea2018Mapper.selectByAll(cnarea2018));
            list.add(pageInfo);
        }
        return list;
    }

}
