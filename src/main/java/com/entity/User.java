package com.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 重构User类，使其实现UserDetails,并使用生成器模式构造参数，以将用户管理功能整合至spring security
 */
@Getter
@AllArgsConstructor
public class User implements Serializable, UserDetails { //定义 'serialVersionUID' 字段的不可序列化类 'User'
    @TableId(value = "uid", type = IdType.AUTO)
    private  Integer uId;

    private final String username;

    private final String password;

    private final String nickname;

    private final Set<GrantedAuthority> authorities;

    @Version
    private Integer version;

    @TableLogic
    private Integer isDeleted;

    @Serial
    private static final long serialVersionUID = 1L;


    private User(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.nickname = builder.nickname;
        this.authorities = builder.authorities;
    }


    /**
     * 默认为所有用户赋予USER权限
     * @return Collection<? extends GrantedAuthority>
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * 生成器模式构造参数
     */
    public static class Builder {
        private String username;
        private String password;
        private String nickname;
        private  Set<GrantedAuthority> authorities;

        public Builder username(String username) {
            this.username = username;
            return this;
        }
        public Builder password(String password) {
            this.password = password;
            return this;
        }
        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }
        public Builder authorities(String role) {
            authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority(role));
            return this;
        }
       public User build() {
            return new User(this);
       }
    }
}