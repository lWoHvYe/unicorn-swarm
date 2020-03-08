package com.lwohvye.springcloud.springcloudlwohvyeprovider.common.aop;

import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel;
import lombok.extern.slf4j.Slf4j;
//import org.apache.shiro.authz.UnauthorizedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Component;

/**
 * @author Hongyan Wang
 * @packageName com.lwohvye.springcloud.springcloudlwohvyeprovider.common.aop
 * @className ControllerAopAspect
 * @description 用于捕获Controller层异常，转化为失败信息，与{@link ResultModel}配合使用
 * @date 2020/1/14 13:45
 */
@Order(2)
@Component
@Aspect
@Slf4j
public class ControllerAopAspect {

    /**
     * @description 定义切点，使用指定实体类ResultModel为返回值的public方法作为切点
     * @params []
     * @author Hongyan Wang
     * @date 2020/1/14 14:24
     */
    @Pointcut("execution(public com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel *(..))")
    public void handlerControllerPointcut() {
    }

    @Around("handlerControllerPointcut()")
    public Object handlerControllerMethod(ProceedingJoinPoint pjp) {
        ResultModel<?> result;
        try {
//            获取程序执行结果
            result = (ResultModel<?>) pjp.proceed();
        } catch (Throwable throwable) {
            result = handlerControllerException(pjp, throwable);
        }
        return result;
    }

    private ResultModel<?> handlerControllerException(ProceedingJoinPoint pjp, Throwable throwable) {
        ResultModel<?> result = new ResultModel<>(throwable);
//        这里可以根据不同的异常，做出不同的操作
//        sql语法错误
        if (throwable instanceof BadSqlGrammarException) {
            result.setMsg(ResultModel.SQL_ERROR_MSG);
            result.setCode(ResultModel.SQL_ERROR_CODE);
        }
//        用户权限不足
//        if (throwable instanceof UnauthorizedException) {
//            result.setMsg(ResultModel.NEED_PERMISSION_MSG);
//            result.setCode(ResultModel.NEED_PERMISSION);
//        }
        return result;
    }
}
