package com.lwohvye.springcloud.springcloudlwohvyeconsumersecurity.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
/**
 * @description 访问发生错误时，跳转到系统统一异常处理页面。首先添加一个GlobalExceptionHandlerAdvice，使用@ControllerAdvice注解：
 * @author Hongyan Wang
 * @date 2020/3/25 15:33
 */
@ControllerAdvice
class GlobalExceptionHandlerAdvice {
    @ExceptionHandler(value = Exception.class)//表示捕捉到所有的异常，你也可以捕捉一个你自定义的异常
    public ModelAndView exception(Exception exception, WebRequest request) {
        ModelAndView modelAndView = new ModelAndView("/error");
        modelAndView.addObject("errorMessage", exception.getMessage());
        modelAndView.addObject("stackTrace", exception.getStackTrace());
        return modelAndView;
    }
}