package com.lwohvye.springcloud.springcloudlwohvyeprovider.common.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 自定义认证过滤器
 */
public class CaptchaFormAuthenticationFilter extends FormAuthenticationFilter {
 
    /**
     * 构造Token,重写Shiro构造Token的方法,增加验证码
     */
    @Override
    protected AuthenticationToken createToken(String username, String password, ServletRequest request, ServletResponse response) {
        // 获取登录请求中用户输入的验证码
        var captchaCode = request.getParameter("captchaCode");
        // 从父类获取是否设置remeberMe
        var rememberMe = super.isRememberMe(request);
        // 从父类获取host
        var host = super.getHost(request);
        // 返回带验证码的Token,Token会被传入Realm, 在Realm中可以取得验证码
        return new CaptchaToken(username, password, captchaCode,rememberMe,host);
    }
 
}
