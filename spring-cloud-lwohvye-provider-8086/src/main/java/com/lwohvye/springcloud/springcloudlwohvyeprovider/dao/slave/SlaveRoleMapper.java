package com.lwohvye.springcloud.springcloudlwohvyeprovider.dao.slave;

import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SlaveRoleMapper {
    Role selectByPrimaryKey(Long id);


}