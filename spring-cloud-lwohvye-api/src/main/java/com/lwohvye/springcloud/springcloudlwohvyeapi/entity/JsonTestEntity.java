package com.lwohvye.springcloud.springcloudlwohvyeapi.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonTestEntity {
    //    编号
    private String indexCode;
    //    名称
    private String name;
    //    是否在线
    private int isOnline;
    //    描述信息
    private String description;
    //    是否置顶
    private boolean isVIP;
    //    创建时间
    private String createTime;
    //    更新时间
    private String updateTime;
    //    坐标
    private String latitude;
    private String longitude;
}
