package com.lwohvye.springcloud.springcloudlwohvyeconsumersecurity.dao;
import java.util.List;

import com.lwohvye.springcloud.springcloudlwohvyeconsumersecurity.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User findByUsername(@Param("username")String username);

    List<User> findAll();



}