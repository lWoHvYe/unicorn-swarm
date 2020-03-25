package com.lwohvye.springcloud.springcloudlwohvyeconsumersecurity.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Transient;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
//User需实现UserDetails接口
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    //id
    private Integer id;

    //用户名
    private String username;

    //密码
    private String password;

    /**
     * 是否锁定
     * true: 未锁定
     * false: 锁定
     */
    private Boolean lockedFlag;
    /*
    角色名称
     */
    private Integer roleId;

    @Transient
    private Role role;

    //security存储权限认证用的
    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 用户账号是否过期
     * true: 未过期
     * false: 已过期
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 用户账号是否被锁定
     * true: 未锁定
     * false: 锁定
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return lockedFlag;
    }

    /**
     * 用户账号凭证(密码)是否过期
     * 简单的说就是可能会因为修改了密码导致凭证过期这样的场景
     * true: 过期
     * false: 无效
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 用户账号是否被启用
     * true: 启用
     * false: 未启用
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
