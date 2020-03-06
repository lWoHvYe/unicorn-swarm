package com.lwohvye.springcloud.springcloudlwohvyeapi.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public class UserLog implements Serializable {
    @ExcelProperty(value = {"标识", "记录ID"}, index = 0)
    private Integer id;
    //  操作用户名
    @ExcelProperty(value = {"操作信息", "操作用户名称"}, index = 1)
    private String username;
    //  操作类型
    @ExcelProperty(value = {"操作信息", "操作类型"}, index = 2)
    private String actType;
    //  操作时间
    @ExcelProperty(value = {"操作信息", "操作时间"}, index = 3)
    //  配置excel中日期的格式
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    private Date actTime;
    //  操作参数
    @ExcelProperty(value = {"操作信息", "操作参数"}, index = 4)
    private String actParams;
    //  操作结果
    @ExcelProperty(value = {"操作信息", "操作结果"}, index = 5)
    private String actResult;
    //  客户机ip地址
    @ExcelProperty(value = {"主机信息", "客户机地址"}, index = 6)
    private String ipAddr;

    private static final long serialVersionUID = 1L;
}