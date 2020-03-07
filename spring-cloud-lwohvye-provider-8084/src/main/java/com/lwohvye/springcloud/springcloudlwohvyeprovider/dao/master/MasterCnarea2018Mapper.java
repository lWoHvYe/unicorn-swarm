package com.lwohvye.springcloud.springcloudlwohvyeprovider.dao.master;

import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.Cnarea2018;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MasterCnarea2018Mapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cnarea2018 record);

    int insertSelective(Cnarea2018 record);

    int updateByPrimaryKeySelective(Cnarea2018 record);

    int updateByPrimaryKey(Cnarea2018 record);
}