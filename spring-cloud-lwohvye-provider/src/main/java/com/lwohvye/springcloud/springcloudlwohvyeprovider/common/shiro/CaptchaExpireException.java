package com.lwohvye.springcloud.springcloudlwohvyeprovider.common.shiro;

import org.apache.shiro.authc.AuthenticationException;

/**
 * @author Hongyan Wang
 * @packageName com.lwohvye.springcloud.springcloudlwohvyeprovider.common.shiro
 * @className CaptchaExpireException
 * @description 自定义验证码过期异常 AuthenticationException为Shiro认证错误的异常,不同错误类型继承该异常即可
 * @date 2020/1/19 15:40
 */
public class CaptchaExpireException extends AuthenticationException {
}
