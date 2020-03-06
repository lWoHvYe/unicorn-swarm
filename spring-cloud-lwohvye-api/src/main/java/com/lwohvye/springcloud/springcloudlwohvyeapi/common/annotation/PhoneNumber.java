package com.lwohvye.springcloud.springcloudlwohvyeapi.common.annotation;


import com.lwohvye.springcloud.springcloudlwohvyeapi.common.validator.PhoneNumberValidator;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Hongyan Wang
 * @description 定义手机号校验注解
 * @date 2020/1/26 13:47
 */
@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface PhoneNumber {
    String message() default "Invalid phone number";

    Class[] groups() default {};

    Class[] payload() default {};
}