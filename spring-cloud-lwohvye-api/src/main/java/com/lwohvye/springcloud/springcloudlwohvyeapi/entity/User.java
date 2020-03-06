package com.lwohvye.springcloud.springcloudlwohvyeapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lwohvye.springcloud.springcloudlwohvyeapi.common.annotation.PhoneNumber;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long uid;
    @Column(unique = true)
//    限制字段不可为空
    @NotBlank(message = "用户名不可为空")
    private String username;
    private String name;//昵称
    //    从json串中移除密码和盐
    @JsonIgnore
    @NotBlank(message = "密码不可为空")
    private String password;
    @JsonIgnore
    private String salt;//加密密码的盐
    private byte state;//用户状态
    //      手机号不可为空且需符合规则
    @PhoneNumber(message = "phoneNumber isn't valid")
    @NotNull(message = "phoneNumber can't be null")
    private String phoneNumber;

    //    一个用户对应一个角色，一个角色对应多个用户，用户是Many端
    @Transient//配置字段不被持久化，即不与数据库字段映射
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)//配置单向立即加载
    @JoinColumn(name = "role_id")
    private Role roles;

    private Long roleId;

    /**
     * 重新定义盐为用户名+salt，增加安全性
     *
     * @return
     */
    @JsonIgnore
    public String getCredentialsSalt() {
        return this.username + this.salt;
    }
}
