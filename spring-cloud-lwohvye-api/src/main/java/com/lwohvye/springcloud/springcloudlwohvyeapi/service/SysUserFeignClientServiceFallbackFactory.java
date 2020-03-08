package com.lwohvye.springcloud.springcloudlwohvyeapi.service;

import com.lwohvye.springcloud.springcloudlwohvyeapi.common.util.PageUtil;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.User;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Hongyan Wang
 * @packageName com.lwohvye.springcloud.springcloudlwohvyeapi.service
 * @className SysUserFeignClientServiceImpl
 * @description
 * @date 2020/3/8 12:17
 */
@Component//记得添加这个注解
public class SysUserFeignClientServiceFallbackFactory implements FallbackFactory<SysUserFeignClientService> {

    @Override
    public SysUserFeignClientService create(Throwable throwable) {
        return new SysUserFeignClientService() {
            @Override
            public ResultModel<PageUtil<User>> list(String username, String order, int page, int pageSize) {
                var resultModel = new ResultModel<PageUtil<User>>();
                resultModel.setCode(ResultModel.SERVER_DOWN);
                resultModel.setMsg(ResultModel.SERVER_DOWN_ERROR_MSG);
                //  查询列表
                return resultModel;
            }

            @Override
            public ResultModel<Integer> delete(Long uid) {
                return null;
            }

            @Override
            public ResultModel<Integer> add(User record) {
                return null;
            }

            @Override
            public User get(Long uid) {
                return null;
            }

            @Override
            public ResultModel<Integer> update(User record) {
                return null;
            }

            @Override
            public User findLoginUser(String username) {
                return null;
            }
        };
    }
}
