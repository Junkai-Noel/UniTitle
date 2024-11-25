package com.security.userManage;

import com.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mapper.UserMapper;
import com.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 *实现了UserDetailsService
 */

@Slf4j
@Component
public class MyUserDetailsService implements UserDetailsManager, UserDetailsPasswordService {


    private final UserMapper userMapper;

    @Autowired
    public MyUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 通过username认证用户
     * @param username String
     * @return user org.springframework.security.core.userdetails.User
     * @throws UsernameNotFoundException 用户名不存在
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User loginUser = userMapper.selectOne(queryWrapper);
        if (loginUser == null) {
            throw new UsernameNotFoundException("未查询到用户："+username);
        }
        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return new User.Builder()
                .username(loginUser.getUsername())
                .password(loginUser.getUsername())
                .nickname(loginUser.getNickname())
                .build();
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }

    @Override
    public void createUser(UserDetails user) {

    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }
}