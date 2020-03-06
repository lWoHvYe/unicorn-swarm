package com.lwohvye.springcloud.springcloudlwohvyeprovider.common.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class CaptchaToken extends UsernamePasswordToken {
    // 序列化ID
    private static final long serialVersionUID = -2804050723838289739L;

    // 验证码
    private String captchaCode;

    /**
     * 构造函数
     * 用户名和密码是登录必须的,因此构造函数中包含两个字段
     */
    public CaptchaToken(String username, String password, String captchaCode, boolean rememberMe, String host) {
        // 父类UsernamePasswordToken的构造函数
        super(username, password, rememberMe, host);
        this.captchaCode = captchaCode;
    }

    /**
     * 获取验证码
     */
    public String getCaptchaCode() {
        return captchaCode;
    }
}
