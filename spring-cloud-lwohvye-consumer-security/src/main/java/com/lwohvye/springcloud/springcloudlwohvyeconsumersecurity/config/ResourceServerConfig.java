package com.lwohvye.springcloud.springcloudlwohvyeconsumersecurity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;

/**
 * 资源服务器配置
 */
@Configuration
//添加资源服务器配置，使用@EnableResourceServer注解开启
@EnableResourceServer
@Order(3)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .requestMatchers().antMatchers("/user/**")
                .and()
                .authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .and()
                .httpBasic();   //配置需要保护的资源路径
    }
}
