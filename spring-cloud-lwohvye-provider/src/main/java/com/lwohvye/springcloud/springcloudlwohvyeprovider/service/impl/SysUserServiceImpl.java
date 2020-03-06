package com.lwohvye.springcloud.springcloudlwohvyeprovider.service.impl;

import cn.hutool.core.util.IdUtil;
import com.lwohvye.springcloud.springcloudlwohvyeapi.common.util.PageUtil;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.User;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.dao.master.MasterUserMapper;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.dao.slave.SlaveRoleMapper;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.dao.slave.SlaveUserMapper;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.repository.UserDao;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.service.SysUserService;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private MasterUserMapper masterUserMapper;
    @Autowired
    private SlaveUserMapper slaveUserMapper;
    @Autowired
    private SlaveRoleMapper slaveRoleMapper;

    @Override
    public PageUtil<User> findUser(String username, PageUtil<User> pageUtil) {
        Page<User> userPage = userDao.findUser(username, pageUtil.obtPageable());
        pageUtil.setPageEntity(userPage);
        return pageUtil;
    }

    @Override
    public int deleteByPrimaryKey(Long uid) {
        return masterUserMapper.deleteByPrimaryKey(uid);
    }

    @Override
    public int insert(User record) {
        return masterUserMapper.insert(record);
    }

    @Override
    public int insertSelective(User user) {
        //        页面传密码时，放进行密码相关操作
        setUserParams(user);
        return masterUserMapper.insertSelective(user);
    }

    @Override
    public User selectByPrimaryKey(Long uid) {
        return slaveUserMapper.selectByPrimaryKey(uid);
    }

    @Override
    public int updateByPrimaryKeySelective(User user) {
        setUserParams(user);
        return masterUserMapper.updateByPrimaryKeySelective(user);
    }

    private void setUserParams(User user) {
        if (!StringUtils.isEmpty(user.getPassword())) {
            //    每次改密码都重新生成盐，提高安全性
            String salt = IdUtil.simpleUUID();
            //    设置盐
            user.setSalt(salt);
            //     使用用户名+密码并反转的方式作为新密码
            String password = new StringBuilder(user.getUsername() + user.getPassword()).reverse().toString();
            //    使用md5加盐加密
            SimpleHash simpleHash =
                    new SimpleHash("md5", password, user.getCredentialsSalt(), 2);
            //    设置密码
            user.setPassword(simpleHash.toString());
        }
    }

    @Override
    public int updateByPrimaryKey(User record) {
        return masterUserMapper.updateByPrimaryKey(record);
    }

    //    开启事务，可以配置事务的参数
    //    PROPAGATION_REQUIRED： 如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务
    //    ISOLATION_DEFAULT: 使用后端数据库默认的隔离级别，Mysql 默认采用的 REPEATABLE_READ隔离级别 Oracle 默认采用的 READ_COMMITTED隔离级别
    //    如果不配置rollbackFor属性,那么事务只会在遇到RuntimeException的时候才会回滚,加上rollbackFor=Exception.class,可以让事务在遇到非运行时异常时也回滚
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class, readOnly = false, timeout = -1)
    @Override
    public User findLoginUser(String username) {
        var user = slaveUserMapper.findByUsername(username);
        if (user != null && user.getRoleId() != null) {
//            获取用户角色信息
            var roles = slaveRoleMapper.selectByPrimaryKey(user.getRoleId());
//            设置用户角色
            user.setRoles(roles);
        }
//        返回结果
        return user;
    }
}

