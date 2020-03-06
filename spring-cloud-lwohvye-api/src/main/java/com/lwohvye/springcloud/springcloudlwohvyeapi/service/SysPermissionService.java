package com.lwohvye.springcloud.springcloudlwohvyeapi.service;


import com.lwohvye.springcloud.springcloudlwohvyeapi.common.util.PageUtil;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.Permission;

public interface SysPermissionService {
    PageUtil<Permission> findPermission(String name, PageUtil<Permission> pageUtil);

    int savePermission(Permission permission);

    int deletePermission(Permission permission);

    int deleteByPrimaryKey(Long id);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);
}

