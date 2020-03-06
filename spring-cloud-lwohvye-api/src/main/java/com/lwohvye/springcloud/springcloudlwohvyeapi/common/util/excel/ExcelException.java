package com.lwohvye.springcloud.springcloudlwohvyeapi.common.util.excel;

/**
 * @author Hongyan Wang
 * @description Excel异常类，用于抛出执行时异常
 * @date 2020/2/26 8:34
 */
public class ExcelException extends RuntimeException {
    public ExcelException(String message) {
        super(message);
    }
}
