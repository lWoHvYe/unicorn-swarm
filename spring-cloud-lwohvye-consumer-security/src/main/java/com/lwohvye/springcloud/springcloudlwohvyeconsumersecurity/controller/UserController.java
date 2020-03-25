package com.lwohvye.springcloud.springcloudlwohvyeconsumersecurity.controller;

import com.lwohvye.springcloud.springcloudlwohvyeconsumersecurity.entity.User;
import com.lwohvye.springcloud.springcloudlwohvyeconsumersecurity.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Hongyan Wang
 * @packageName com.lwohvye.springcloud.springcloudlwohvyeconsumersecurity.controller
 * @className UserController
 * @description
 * @date 2020/3/25 18:46
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    //指定角色为ROLE_admin或ROLE_user才能访问
    @PreAuthorize(value = "hasAnyRole('admin','user')")
    @RequestMapping("/get")
    public UserDetails getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = null;
        if (principal instanceof UserDetails)
            userDetails = (UserDetails) principal;
        return userDetails;
    }

    //指定需要角色，因为有公共前缀ROLE_在，角色需为ROLE_admin才行
    @PreAuthorize(value = "hasRole('admin')")
    @RequestMapping("/list")
    public List<User> list() {
        return userService.list();
    }
}
