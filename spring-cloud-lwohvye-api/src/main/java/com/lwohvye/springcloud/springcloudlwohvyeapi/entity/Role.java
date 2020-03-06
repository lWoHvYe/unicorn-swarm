package com.lwohvye.springcloud.springcloudlwohvyeapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    @NotBlank(message = "角色名不可为空")
    private String roleName;
    private String description;//角色描述
    private Boolean available = Boolean.FALSE;//是否可用


    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)//单向立即加载
    // 设置关联表、关联字段(主键)及副表的关联字段(主键)
    @JoinTable(name = "role_permission", joinColumns = {@JoinColumn(name = "role_id")}, inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    private List<Permission> permissions;

    @JsonIgnore//不序列化该属性
//    一个角色对应多个用户，角色是one端
    @OneToMany(targetEntity = User.class)
    private List<User> users;
}
