package com.lwohvye.springcloud.springcloudlwohvyeapi.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

// propagate value from up-stream to down-stream for FeignClint call
@Slf4j
@Component
public class FeignClientHeaderModifierInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        var requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            var webRequest = servletRequestAttributes.getRequest();
            var headerName = "User-Token";
            var userToken = webRequest.getHeader(headerName);
            if (Objects.nonNull(userToken) && !requestTemplate.headers().containsKey(headerName)) {
                requestTemplate.header(headerName, userToken);
            }
        }
    }
}
