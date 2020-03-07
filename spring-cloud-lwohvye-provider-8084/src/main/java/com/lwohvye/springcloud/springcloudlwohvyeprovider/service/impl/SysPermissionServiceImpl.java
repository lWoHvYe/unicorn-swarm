package com.lwohvye.springcloud.springcloudlwohvyeprovider.service.impl;

import com.lwohvye.springcloud.springcloudlwohvyeapi.common.util.PageUtil;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.Permission;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.dao.master.MasterPermissionMapper;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.dao.slave.SlavePermissionMapper;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.repository.PermissionDao;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class SysPermissionServiceImpl implements SysPermissionService {
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private MasterPermissionMapper masterPermissionMapper;
    @Autowired
    private SlavePermissionMapper slavePermissionMapper;

    @Override
    public PageUtil<Permission> findPermission(String name, PageUtil<Permission> pageUtil) {
        Page<Permission> permissionPage = permissionDao.findPermission(name, pageUtil.obtPageable());
        pageUtil.setPageEntity(permissionPage);
        return pageUtil;
    }

    @Override
    public int savePermission(Permission permission) {
        permissionDao.save(permission);
        return 1;
    }

    @Override
    public int deletePermission(Permission permission) {
        permissionDao.delete(permission);
        return 1;
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return masterPermissionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Permission record) {
        return masterPermissionMapper.insert(record);
    }

    @Override
    public int insertSelective(Permission record) {
        return masterPermissionMapper.insertSelective(record);
    }

    @Override
    public Permission selectByPrimaryKey(Long id) {
        return slavePermissionMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Permission record) {
        return masterPermissionMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Permission record) {
        return masterPermissionMapper.updateByPrimaryKey(record);
    }
}

