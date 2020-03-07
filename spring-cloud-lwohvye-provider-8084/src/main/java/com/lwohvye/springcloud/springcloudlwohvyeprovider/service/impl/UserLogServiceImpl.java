package com.lwohvye.springcloud.springcloudlwohvyeprovider.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.UserLog;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.dao.master.MasterUserLogMapper;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.dao.slave.SlaveUserLogMapper;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.service.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLogServiceImpl implements UserLogService {

    @Autowired
    private MasterUserLogMapper masterUserLogMapper;
    @Autowired
    private SlaveUserLogMapper slaveUserLogMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return masterUserLogMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserLog record) {
        return masterUserLogMapper.insert(record);
    }

    @Override
    public int insertSelective(UserLog record) {
        return masterUserLogMapper.insertSelective(record);
    }

    @Override
    public UserLog selectByPrimaryKey(Integer id) {
        return slaveUserLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserLog record) {
        return masterUserLogMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserLog record) {
        return masterUserLogMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageInfo<UserLog> list(String username, String startDate, String endDate, int page, int pageSize) {
        return PageHelper.startPage(page, pageSize, "act_time desc").doSelectPageInfo(
                () -> slaveUserLogMapper.list(username, startDate, endDate)
        );
    }

}
