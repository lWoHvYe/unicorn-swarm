package com.lwohvye.springcloud.springcloudlwohvyeprovider.dao.master;

import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.UserLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MasterUserLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserLog record);

    int insertSelective(UserLog record);

    int updateByPrimaryKeySelective(UserLog record);

    int updateByPrimaryKey(UserLog record);
}