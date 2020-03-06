package com.lwohvye.springcloud.springcloudlwohvyeprovider.common.shiro;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 自定义验证码错误异常
 * AuthenticationException为Shiro认证错误的异常,不同错误类型继承该异常即可
 */
public class CaptchaErrorException extends AuthenticationException {
}
