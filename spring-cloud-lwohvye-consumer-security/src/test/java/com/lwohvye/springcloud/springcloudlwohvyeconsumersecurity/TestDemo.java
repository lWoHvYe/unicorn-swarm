package com.lwohvye.springcloud.springcloudlwohvyeconsumersecurity;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Hongyan Wang
 * @packageName PACKAGE_NAME
 * @className Test
 * @description
 * @date 2020/3/25 18:12
 */
public class TestDemo {
    @Test
    public void test(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("root"));
    }

    @Test
    public void test1(){
        String a = "2";
        String b = "5";
        System.out.println(a+b);
        System.out.println(
                (a+b).getClass()
        );
    }
}
