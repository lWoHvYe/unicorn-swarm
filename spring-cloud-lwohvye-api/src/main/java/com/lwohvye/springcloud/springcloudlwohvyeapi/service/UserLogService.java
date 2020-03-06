package com.lwohvye.springcloud.springcloudlwohvyeapi.service;

import com.github.pagehelper.PageInfo;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.UserLog;

public interface UserLogService {


    int deleteByPrimaryKey(Integer id);

    int insert(UserLog record);

    int insertSelective(UserLog record);

    UserLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserLog record);

    int updateByPrimaryKey(UserLog record);

    PageInfo<UserLog> list(String username, String startDate, String endDate, int page, int pageSize);

}
