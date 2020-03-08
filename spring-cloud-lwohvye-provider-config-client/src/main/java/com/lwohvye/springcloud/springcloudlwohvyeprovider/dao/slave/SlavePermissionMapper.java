package com.lwohvye.springcloud.springcloudlwohvyeprovider.dao.slave;

import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SlavePermissionMapper {
    Permission selectByPrimaryKey(Long id);
}