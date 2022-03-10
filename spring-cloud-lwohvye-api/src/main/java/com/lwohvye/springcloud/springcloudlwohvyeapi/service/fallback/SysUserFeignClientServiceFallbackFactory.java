package com.lwohvye.springcloud.springcloudlwohvyeapi.service.fallback;

import com.lwohvye.springcloud.springcloudlwohvyeapi.common.util.PageUtil;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.User;
import com.lwohvye.springcloud.springcloudlwohvyeapi.service.SysUserFeignClientService;
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
                return resultModel;
            }

            @Override
            public ResultModel<Integer> delete(Long uid) {
                var resultModel = new ResultModel<Integer>();
                resultModel.setCode(ResultModel.SERVER_DOWN);
                resultModel.setMsg(ResultModel.SERVER_DOWN_ERROR_MSG);
                return resultModel;
            }

            @Override
            public ResultModel<Integer> add(User record) {
                var resultModel = new ResultModel<Integer>();
                resultModel.setCode(ResultModel.SERVER_DOWN);
                resultModel.setMsg(ResultModel.SERVER_DOWN_ERROR_MSG);
                return resultModel;
            }

            @Override
            public User get(Long uid) {
                return null;
            }

            @Override
            public ResultModel<Integer> update(User record) {
                var resultModel = new ResultModel<Integer>();
                resultModel.setCode(ResultModel.SERVER_DOWN);
                resultModel.setMsg(ResultModel.SERVER_DOWN_ERROR_MSG);
                return resultModel;
            }

            @Override
            public User findLoginUser(String username) {
                return null;
            }
        };
    }
}
