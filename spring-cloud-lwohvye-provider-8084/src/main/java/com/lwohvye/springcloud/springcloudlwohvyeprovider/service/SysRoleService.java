package com.lwohvye.springcloud.springcloudlwohvyeprovider.service;


import com.lwohvye.springcloud.springcloudlwohvyeapi.common.util.PageUtil;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.Role;

public interface SysRoleService {
    PageUtil<Role> findRole(String roleName, PageUtil<Role> pageUtil);

    int saveRole(Role role, String permissionId);

    int deleteRole(Role role);

    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}

