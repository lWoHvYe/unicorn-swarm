package com.lwohvye.springcloud.springcloudlwohvyeapi.service.fallback;

import com.github.pagehelper.PageInfo;
import com.lwohvye.springcloud.springcloudlwohvyeapi.common.util.PageUtil;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.Cnarea2018;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.User;
import com.lwohvye.springcloud.springcloudlwohvyeapi.service.Cnarea2018FeignClientService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Cnarea2018FeignClientServiceFallbackFactory implements FallbackFactory<Cnarea2018FeignClientService> {
    @Override
    public Cnarea2018FeignClientService create(Throwable cause) {
        return new Cnarea2018FeignClientService() {
            @Override
            public ResultModel<List<PageInfo<Cnarea2018>>> list(String province, String levels, String page, String pageSize) {
                var resultModel = new ResultModel<List<PageInfo<Cnarea2018>>>();
                resultModel.setCode(ResultModel.SERVER_DOWN);
                resultModel.setMsg(ResultModel.SERVER_DOWN_ERROR_MSG);
                return resultModel;
            }

            @Override
            public ResultModel<List<PageInfo<Cnarea2018>>> listSingle(String province, String levels, int page, int pageSize) {
                var resultModel = new ResultModel<List<PageInfo<Cnarea2018>>>();
                resultModel.setCode(ResultModel.SERVER_DOWN);
                resultModel.setMsg(ResultModel.SERVER_DOWN_ERROR_MSG);
                return resultModel;
            }
        };
    }
}
