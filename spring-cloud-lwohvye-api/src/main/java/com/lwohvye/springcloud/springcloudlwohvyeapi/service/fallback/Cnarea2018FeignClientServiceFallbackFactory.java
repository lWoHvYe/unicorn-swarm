package com.lwohvye.springcloud.springcloudlwohvyeapi.service.fallback;

import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.Cnarea2018;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel;
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
            public ResultModel<List<Cnarea2018>> list() {
                var resultModel = new ResultModel<List<Cnarea2018>>();
                resultModel.setCode(ResultModel.SERVER_DOWN);
                resultModel.setMsg(ResultModel.SERVER_DOWN_ERROR_MSG);
                return resultModel;
            }
        };
    }
}
