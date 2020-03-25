package com.lwohvye.springcloud.springcloudlwohvyeconsumersecurity.service.impl;

import com.lwohvye.springcloud.springcloudlwohvyeconsumersecurity.entity.Role;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.lwohvye.springcloud.springcloudlwohvyeconsumersecurity.dao.UserMapper;
import com.lwohvye.springcloud.springcloudlwohvyeconsumersecurity.entity.User;
import com.lwohvye.springcloud.springcloudlwohvyeconsumersecurity.service.UserService;

import java.util.List;
import java.util.TreeSet;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(User record) {
        return userMapper.insert(record);
    }

    @Override
    public int insertSelective(User record) {
        return userMapper.insertSelective(record);
    }

    @Override
    public User selectByPrimaryKey(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(User record) {
        return userMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(User record) {
        return userMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<User> list() {
        return userMapper.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        user.setAuthorities(new TreeSet<>() {
            {
                add(user.getRole());
            }
        });
        return user;
    }
}
