package com.lwohvye.springcloud.springcloudlwohvyeconsumersecurity.service;

import com.lwohvye.springcloud.springcloudlwohvyeconsumersecurity.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {


    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> list();
}
