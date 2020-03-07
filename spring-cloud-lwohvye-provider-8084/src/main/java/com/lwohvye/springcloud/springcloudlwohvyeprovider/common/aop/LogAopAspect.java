package com.lwohvye.springcloud.springcloudlwohvyeprovider.common.aop;

import com.lwohvye.springcloud.springcloudlwohvyeapi.common.util.HttpContextUtil;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.User;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.UserLog;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.common.annotation.LogAnno;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.service.UserLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;

/**
 * AOP实现日志
 *
 * @author Hongyan Wang
 */
@Order(3)
@Component
@Aspect
public class LogAopAspect {

    @Autowired
    private UserLogService userLogService;

    /**
     * 环绕通知记录日志通过注解匹配到需要增加日志功能的方法
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.lwohvye.springcloud.springcloudlwohvyeprovider.common.annotation.LogAnno)")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        // 1.方法执行前的处理，相当于前置通知
        // 获取方法签名
        var methodSignature = (MethodSignature) pjp.getSignature();
        // 获取方法
        var method = methodSignature.getMethod();
        // 获取方法名
        var methodName = method.getName();
        // 获取类名
        var targetName = pjp.getTarget().getClass().getName();
        // 获取方法上面的注解
        var logAnno = method.getAnnotation(LogAnno.class);
        // 获取操作描述的属性值
        var operateType = logAnno.operateType();
        // 创建一个日志对象(准备记录日志)
        var log = new UserLog();
        var actType = new StringBuilder();
        actType.append("类名 : ").append(targetName).append(" ; 方法名 : ").append(methodName);
        if (!StringUtils.isEmpty(operateType))
            actType.append(" ; 方法描述 : ").append(operateType);
        log.setActType(actType.toString());// 操作说明
//        设置方法参数
        var param = getStrParams(pjp, methodSignature);
        log.setActParams(param);

        try {
            // 设置操作人，从session中获取
            var session = HttpContextUtil.getRequest().getSession();
            var curUser = (User) session.getAttribute("curUser");
            if (curUser != null)
                log.setUsername(curUser.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }

        var ip = HttpContextUtil.getIpAddress();
        log.setIpAddr(ip);
        // 让代理方法执行
        var result = pjp.proceed();
        var doResult = "方法执行出错";
        if (result != null) {
            if (result.toString().contains("flag=true")) {
                doResult = "方法执行成功";
            } else if (result.toString().contains("flag=false")) {
                doResult = "方法执行失败";
            }
        }
//        记录执行结果
        log.setActResult(doResult);
        userLogService.insertSelective(log);// 添加日志记录
        return result;
    }

    /**
     * @return java.lang.String
     * @description 获取方法的参数
     * @params [pjp, methodSignature]
     * @author Hongyan Wang
     * @date 2019/12/26 10:19
     */
    @NotNull
    private String getStrParams(ProceedingJoinPoint pjp, MethodSignature methodSignature) {
        var paraNames = methodSignature.getParameterNames();
        var args = pjp.getArgs();
        var sb = new StringBuilder();
        if (paraNames != null && paraNames.length > 0 && args != null && args.length > 0) {
            for (var i = 0; i < paraNames.length; i++) {
                sb.append(paraNames[i]).append(":").append(args[i]).append(",");
            }
//            字符截取应放在方法内，否则若方法无参数，会报错
            sb.delete(sb.length() - 1, sb.length() + 1);
        }
        return sb.toString();
    }

}