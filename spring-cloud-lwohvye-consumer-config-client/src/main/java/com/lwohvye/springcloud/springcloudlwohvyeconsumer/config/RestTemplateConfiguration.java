package com.lwohvye.springcloud.springcloudlwohvyeconsumer.config;

import com.lwohvye.springcloud.springcloudlwohvyeapi.interceptor.RestTemplateHeaderModifierInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfiguration //boot -->spring   applicationContext.xml --- @Configuration配置   ConfigBean = applicationContext.xml
{

    static Logger LOGGER = LoggerFactory.getLogger(RestTemplateConfiguration.class);

    //RestTemplate提供了多种便捷访问远程Http服务的方法，
    //是一种简单便捷的访问restful服务模板类，是Spring提供的用于访问Rest服务的客户端模板工具集
    @Bean
    @LoadBalanced//Spring Cloud Ribbon是基于Netflix Ribbon实现的一套客户端       负载均衡的工具。
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = null;
//        if (LOGGER.isDebugEnabled()) {
        ClientHttpRequestFactory factory
                = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        restTemplate = new RestTemplate(factory);
//        } else {
//            restTemplate = new RestTemplate();
//        }

        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        if (CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(new RestTemplateHeaderModifierInterceptor());
        interceptors.add(new RestTemplateLoggingInterceptor());
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}
