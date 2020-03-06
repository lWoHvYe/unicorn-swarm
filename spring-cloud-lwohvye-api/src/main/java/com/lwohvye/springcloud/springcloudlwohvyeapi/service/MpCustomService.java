package com.lwohvye.springcloud.springcloudlwohvyeapi.service;


import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.MpCustomEntity;

import java.util.List;

/**
 * Service接口
 *
 * @author author
 * @since 2019-10-08
 */
public interface MpCustomService {

    List<MpCustomEntity> list();

    int deleteByPrimaryKey(Integer customId);

    int insert(MpCustomEntity record);

    MpCustomEntity insertSelective(MpCustomEntity record);

    MpCustomEntity selectByPrimaryKey(Integer customId);

    MpCustomEntity updateByPrimaryKeySelective(MpCustomEntity record);

    int updateByPrimaryKey(MpCustomEntity record);
}

