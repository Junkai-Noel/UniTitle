package com.userManage;

import com.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mapper.UserMapper;
import com.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 *实现了UserDetailsService
 */

@Slf4j
@Component
public class MyUserDetailsService implements UserDetailsService{


    private final UserMapper userMapper;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public MyUserDetailsService(UserMapper userMapper,
                                UserServiceImpl userServiceImpl) {
        this.userMapper = userMapper;
        this.userServiceImpl = userServiceImpl;
    }

    /**
     * 通过username从数据库中获取用户信息
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
        return new MyUserDetails(loginUser);

    }
}