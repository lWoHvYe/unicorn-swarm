package com.lwohvye.springcloud.springcloudlwohvyeapi.service;


import com.lwohvye.springcloud.springcloudlwohvyeapi.common.util.PageUtil;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.User;

public interface SysUserService {

    PageUtil<User> findUser(String username, PageUtil<User> pageable);

    int deleteByPrimaryKey(Long uid);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long uid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * @description 由于配置了懒加载，项目存在部分问题，登陆验证时需手动获取用户角色及权限信息
     * @params [username]
     * @return com.lwohvye.springboot.dubbo.entity.User
     * @author Hongyan Wang
     * @date 2019/12/5 10:28
     */
    User findLoginUser(String username);
}

