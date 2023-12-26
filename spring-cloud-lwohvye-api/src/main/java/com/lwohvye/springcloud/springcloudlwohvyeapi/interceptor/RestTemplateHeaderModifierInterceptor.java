package com.lwohvye.springcloud.springcloudlwohvyeapi.interceptor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.Objects;

// propagate value from up-stream to down-stream for RestTemplate call

@Slf4j
@AllArgsConstructor
public class RestTemplateHeaderModifierInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        var requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            var webRequest = servletRequestAttributes.getRequest();
            var headerName = "User-Token";
            var userToken = webRequest.getHeader(headerName);
            if (Objects.nonNull(userToken) && !request.getHeaders().containsKey(headerName)) {
                request.getHeaders().add(headerName, userToken);
            }
        }
        return execution.execute(request, body);
    }
}
