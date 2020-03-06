package com.lwohvye.springcloud.springcloudlwohvyeapi.common.validator;


import com.lwohvye.springcloud.springcloudlwohvyeapi.common.annotation.PhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Hongyan Wang
 * @description 手机号校验
 * @date 2020/1/26 11:26
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public boolean isValid(String phoneField, ConstraintValidatorContext context) {
        if (phoneField == null) {
            // can be null
            return true;
        }
        return phoneField.
                matches("^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$")
               && phoneField.length() > 8 && phoneField.length() < 14;
    }
}