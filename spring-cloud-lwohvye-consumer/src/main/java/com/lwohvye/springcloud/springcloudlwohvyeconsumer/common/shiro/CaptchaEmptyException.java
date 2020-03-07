package com.lwohvye.springcloud.springcloudlwohvyeconsumer.common.shiro;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 自定义验证码为空异常
 * AuthenticationException为Shiro认证错误的异常,不同错误类型继承该异常即可
 */
public class CaptchaEmptyException extends AuthenticationException {
}
 
