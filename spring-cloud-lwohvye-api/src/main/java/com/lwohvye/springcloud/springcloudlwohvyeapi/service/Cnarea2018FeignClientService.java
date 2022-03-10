package com.lwohvye.springcloud.springcloudlwohvyeapi.service;

import com.lwohvye.springcloud.springcloudlwohvyeapi.api.Cnarea2018API;
import com.lwohvye.springcloud.springcloudlwohvyeapi.service.fallback.Cnarea2018FeignClientServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "LWOHVYE-PROVIDER",fallbackFactory = Cnarea2018FeignClientServiceFallbackFactory.class)
public interface Cnarea2018FeignClientService extends Cnarea2018API {
}
