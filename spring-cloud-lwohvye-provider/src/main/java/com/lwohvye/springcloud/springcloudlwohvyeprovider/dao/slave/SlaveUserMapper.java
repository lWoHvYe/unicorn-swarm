package com.lwohvye.springcloud.springcloudlwohvyeprovider.dao.slave;

import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SlaveUserMapper {
    User selectByPrimaryKey(Long uid);

    List<User> selectByAll(User user);

    User findByUsername(@Param("username") String username);


}