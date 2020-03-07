package com.lwohvye.springcloud.springcloudlwohvyeapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "permission")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "权限名不可为空")
    private String name;
    @Column(columnDefinition = "enum('menu','button')")
    private String resourceType;//资源类型,[menu|button]
    private String url;//资源路径
    @NotBlank(message = "权限不可为空")
    private String permissionStr;//权限字符串
    private Long parentId;//父编号
    private String parentIds;//父编号列表
    private Boolean available = Boolean.FALSE;//是否可用

    @JsonIgnore//不序列化该属性
    @ManyToMany(fetch = FetchType.LAZY)//配置反向延迟加载
    @JoinTable(name = "role_permission", joinColumns = {@JoinColumn(name = "permission_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles;
}
