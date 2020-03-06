package com.lwohvye.springcloud.springcloudlwohvyeprovider.service.impl;

import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.MpCustomEntity;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.common.redis.RedisKeys;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.dao.master.MasterMpCustomMapper;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.dao.slave.SlaveMpCustomMapper;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.service.MpCustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Hongyan Wang
 * @packageName com.lwohvye.springcloud.springcloudlwohvyeprovider.serviceimpl
 * @interfaceName MpCustomServiceImpl
 * @description 使用redis缓存，分别缓存客户列表及单个客户两种数据，
 * 执行添加操作时，清除客户列表缓存
 * 执行修改、删除操作时，清除客户列表和对应客户缓存
 * 将缓存放在Service层，方便使用shiro进行权限控制
 * 使用缓存时，对应接口的返回类型需设置为Object，这一点需特别注意
 * @date 2019/10/10 13:37
 */
@Service
//CacheConfig注解用来配置缓存中一些公共属性的值
@CacheConfig(cacheNames = "mpCustom")
public class MpCustomServiceImpl implements MpCustomService {

    @Autowired
    private MasterMpCustomMapper masterMpCustomMapper;
    @Autowired
    private SlaveMpCustomMapper slaveMpCustomMapper;

    /**
     * @return java.util.List<com.lwohvye.springboot.dubbo.entity.MpCustomEntity>
     * @description 获取企业列表，
     * @params []
     * @author Hongyan Wang
     * @date 2019/10/10 16:09
     */
    @Cacheable(key = "'mpCustomList'")
    @Override
    public List<MpCustomEntity> list() {
        return slaveMpCustomMapper.list();
    }

    /**
     * @return void
     * @description 根据id删除企业，并清除对应的缓存，对于需要对多个key执行操作，使用@Caching注解
     * 设置beforeInvocation = true，是防止方法执行抛异常导致缓存未被清
     * @params [customId]
     * @author Hongyan Wang
     * @date 2019/10/10 14:28
     */
    @Caching(evict = {
            @CacheEvict(key = "'mpCustomList'", beforeInvocation = true),
            @CacheEvict(key = "'com.lwohvye.springcloud.springcloudlwohvyeprovider.serviceimpl.MpCustomServiceImpl_searchById_'+#customId",
                    cacheNames = "mpCustom::" + RedisKeys.REDIS_EXPIRE_TIME_KEY + "=600", beforeInvocation = true)
    })
    @Override
    public int deleteByPrimaryKey(Integer customId) {
        return masterMpCustomMapper.deleteByPrimaryKey(customId);
    }

    @Override
    public int insert(MpCustomEntity record) {
        return masterMpCustomMapper.insert(record);
    }

    /**
     * @return com.lwohvye.springboot.dubbo.entity.MpCustomEntity
     * @description 添加客户，清空客户列表缓存并将新加客户加入缓存
     * @params [mpCustomEntity]
     * @author Hongyan Wang
     * @date 2019/10/10 17:10
     */
    @Caching(evict = {@CacheEvict(key = "'mpCustomList'", beforeInvocation = true)},
            put = {@CachePut(key = "'com.lwohvye.springcloud.springcloudlwohvyeprovider.serviceimpl.MpCustomServiceImpl_searchById_'+#mpCustomEntity.customId",
                    cacheNames = "mpCustom::" + RedisKeys.REDIS_EXPIRE_TIME_KEY + "=600")}
    )
    @Override
    public MpCustomEntity insertSelective(MpCustomEntity mpCustomEntity) {
        masterMpCustomMapper.insertSelective(mpCustomEntity);
        return mpCustomEntity;
    }

    /**
     * @return com.lwohvye.springboot.dubbo.entity.MpCustomEntity
     * @description 根据id查询客户信息，使用缓存，key为默认生成策略生成 完整类名 + 方法名 + 参数值
     * 因为空数据不应放入缓存，故使用unless属性进行排除，当判断为true时，不缓存
     * @params [customId]
     * @author Hongyan Wang
     * @date 2019/10/10 16:31
     */
    @Cacheable(unless = "#result == null", cacheNames = "mpCustom::" + RedisKeys.REDIS_EXPIRE_TIME_KEY + "=600")
    @Override
    public MpCustomEntity selectByPrimaryKey(Integer customId) {
        return slaveMpCustomMapper.selectByPrimaryKey(customId);
    }

    /**
     * @return com.lwohvye.springboot.dubbo.entity.MpCustomEntity
     * @description 修改客户信息，需要清除客户列表及对应客户的缓存，并重新添加对应客户的缓存，所以需方法返回修改后的客户
     * @params [mpCustomEntity]
     * @author Hongyan Wang
     * @date 2019/10/10 17:13
     */
    @Caching(evict = {
            @CacheEvict(key = "'mpCustomList'", beforeInvocation = true),
            @CacheEvict(key = "'com.lwohvye.springcloud.springcloudlwohvyeprovider.serviceimpl.MpCustomServiceImpl_searchById_'+#mpCustomEntity.customId",
                    beforeInvocation = true, cacheNames = "mpCustom::" + RedisKeys.REDIS_EXPIRE_TIME_KEY + "=600")},
            put = {@CachePut(key = "'com.lwohvye.springcloud.springcloudlwohvyeprovider.serviceimpl.MpCustomServiceImpl_searchById_'+#mpCustomEntity.customId",
                    cacheNames = "mpCustom::" + RedisKeys.REDIS_EXPIRE_TIME_KEY + "=600")}
    )
    @Override
    public MpCustomEntity updateByPrimaryKeySelective(MpCustomEntity mpCustomEntity) {
        masterMpCustomMapper.updateByPrimaryKeySelective(mpCustomEntity);
        return slaveMpCustomMapper.searchById(mpCustomEntity.getCustomId());
    }

    @Override
    public int updateByPrimaryKey(MpCustomEntity record) {
        return masterMpCustomMapper.updateByPrimaryKey(record);
    }
}

