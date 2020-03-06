package com.lwohvye.springcloud.springcloudlwohvyeprovider.service;

import com.github.pagehelper.PageInfo;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.Cnarea2018;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Cnarea2018Service {


    int deleteByPrimaryKey(Integer id);

    int insert(Cnarea2018 record);

    int insertSelective(Cnarea2018 record);

    Cnarea2018 selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cnarea2018 record);

    int updateByPrimaryKey(Cnarea2018 record);

    CompletableFuture<PageInfo<Cnarea2018>> list(String province, Integer level, int page, int pageSize);

    List<String> listProName();

    List<PageInfo<Cnarea2018>> listSingle(List<String> proList, Integer level, int page, int pageSize);
}
