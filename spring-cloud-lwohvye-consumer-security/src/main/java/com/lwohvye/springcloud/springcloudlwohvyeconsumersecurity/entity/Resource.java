package com.lwohvye.springcloud.springcloudlwohvyeconsumersecurity.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;
    //id
    protected Integer id;

    //资源名称
    private String resourceName;

    //资源标识
    private String resourceKey;

    //资源url
    private String url;

    /**
     * 资源类型
     * 0:菜单
     * 1:按钮
     */
    private Integer type;

}
